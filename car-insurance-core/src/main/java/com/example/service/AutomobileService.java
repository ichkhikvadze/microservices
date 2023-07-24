package com.example.service;

import com.example.Automobile;
import com.example.Car;
import com.example.Insured;
import com.example.enums.InsuranceType;
import com.example.exception.*;
import com.example.facade.CarFacade;
import com.example.mapper.CarToAutomobileMapper;
import com.example.mapper.OwnerToInsuredMapper;
import com.example.repository.AutomobileRepository;
import com.example.repository.InsuredRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AutomobileService {

    private static final int PERCENT_OF_FULL_INSURANCE_PRICE = 3;
    private static final int OLD_CAR_PERCENT = 1;
    private static final int OWNER_AGE_PERCENT = 1;
    private static final int PERCENT_OF_FRANCHISE_PRICE = 10;
    private static final int PERCENT_FOR_MAX_FRANCHISE_PRICE = 1;
    private static final int PERCENT_OF_UNLIMITED = 1;

    private static final int CAR_YEAR_UPPER_LIMIT = 25;
    private static final int CAR_YEAR_LOWER_LIMIT = 10;
    private static final int OWNER_YEAR_UPPER_LIMIT = 24;
    private static final int OWNER_YEAR_LOWER_LIMIT = 18;

    private final AutomobileRepository repository;
    private final InsuredRepository insuredRepository;
    private final CarFacade carFacade;

    @Autowired
    public AutomobileService(AutomobileRepository repository,
                             InsuredRepository insuredRepository,
                             CarFacade carFacade) {
        this.repository = repository;
        this.insuredRepository = insuredRepository;
        this.carFacade = carFacade;
    }

    public Iterable<Automobile> findAllAutomobiles(final int page, final int pageSize) {
        log.info(String.format("Find %s automobiles on %s page", pageSize, page));
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable).stream()
                .collect(Collectors.toList());
    }

    public Automobile createAutomobile(final String licensePlate,
                                       final String personalNo,
                                       final BigDecimal franchisePrice,
                                       final BigDecimal fullInsurancePrice,
                                       final InsuranceType insuranceType) throws URISyntaxException {
        log.info(String.format("Create car with license plate = %s of person with personal no = %s", licensePlate, personalNo));
        Car car = carFacade.getCar(personalNo, licensePlate).orElseThrow(() -> {
            String exceptionMessage = String.format(
                    "Car with license plate = %s of person with personal no = %s not found",
                    licensePlate,
                    personalNo
            );
            log.debug(exceptionMessage);
            return new CarNotFoundException(exceptionMessage);
        });
        checkCarDataValidation(car, licensePlate, franchisePrice, fullInsurancePrice);
        if (!insuredRepository.existsByPersonalNo(personalNo)) {
            Insured newInsured = OwnerToInsuredMapper.mapOwnerToInsured(car.getOwner());
            insuredRepository.save(newInsured);
        }
        Insured insured = insuredRepository.findByPersonalNo(personalNo).orElseThrow(() -> {
            String exceptionMessage = String.format("Insured with personal no = %s not found", personalNo);
            log.debug(exceptionMessage);
            return new InsuredNotFoundException(exceptionMessage);
        });
        int insurancePensionPercent = calculateInsurancePensionPercent(car, insuranceType);
        BigDecimal insurancePension = calculateInsurancePension(car, fullInsurancePrice, insurancePensionPercent);
        Automobile automobile = CarToAutomobileMapper.mapCarToAutomobile(
                car,
                licensePlate,
                fullInsurancePrice,
                null,
                insuranceType,
                null
        );
        if (insuranceType.equals(InsuranceType.FRANCHISE)) {
            insurancePension = insurancePension
                    .subtract(
                            franchisePrice
                                    .multiply(BigDecimal.valueOf(PERCENT_OF_FRANCHISE_PRICE))
                                    .divide(BigDecimal.valueOf(100))
                    );
            automobile.setFranchisePrice(franchisePrice);
        }
        automobile.setInsurancePension(insurancePension);
        automobile.setInsured(insured);
        return repository.save(automobile);
    }

    private BigDecimal calculateInsurancePension(Car car, BigDecimal fullInsurancePrice, int insurancePensionPercent) {
        return fullInsurancePrice
                .multiply(BigDecimal.valueOf(insurancePensionPercent))
                .divide(BigDecimal.valueOf(100));
    }

    private int calculateInsurancePensionPercent(Car car, InsuranceType insuranceType) {
        int insurancePensionPercent = PERCENT_OF_FULL_INSURANCE_PRICE;
        if (isBetweenYears(car.getReleaseDate(), CAR_YEAR_LOWER_LIMIT, CAR_YEAR_UPPER_LIMIT)) {
            insurancePensionPercent += OLD_CAR_PERCENT;
        }
        if (isBetweenYears(car.getOwner().getBirthDate(), OWNER_YEAR_LOWER_LIMIT, OWNER_YEAR_UPPER_LIMIT)) {
            insurancePensionPercent += OWNER_AGE_PERCENT;
        }
        if (insuranceType.equals(InsuranceType.UNLIMITED)) {
            insurancePensionPercent += PERCENT_OF_UNLIMITED;
        }
        return insurancePensionPercent;
    }


    private boolean isBetweenYears(LocalDate date, int lowerYearLimit, int upperYearLimit) {
        int year = date.until(LocalDate.now()).getYears();
        return year >= lowerYearLimit && year <= upperYearLimit;
    }

    private void checkCarDataValidation(Car car,
                                        String licensePlate,
                                        BigDecimal franchisePrice,
                                        BigDecimal fullInsurancePrice) {
        if (repository.existsByLicensePlate(licensePlate)) {
            String exceptionMessage = String.format("Automobile with license plate %s already exists", licensePlate);
            log.debug(exceptionMessage);
            throw new LicensePlateAlreadyExistsException(exceptionMessage);
        }
        if (car.getReleaseDate().until(LocalDate.now()).getYears() >= CAR_YEAR_UPPER_LIMIT) {
            String exceptionMessage = String.format("Car is %s years old. older than %s year.", car, CAR_YEAR_UPPER_LIMIT);
            log.debug(exceptionMessage);
            throw new InsuranceCriteriaMismatchException(exceptionMessage);
        } else if (isFranchiseExceeded(franchisePrice, fullInsurancePrice)) {
            String exceptionMessage = "Franchise price exceeded";
            log.debug(exceptionMessage);
            throw new InsuranceCriteriaMismatchException(exceptionMessage);
        }
    }

    private boolean isFranchiseExceeded(BigDecimal franchisePrice, BigDecimal fullInsurancePrice) {
        BigDecimal maxLegalFranchisePrice = fullInsurancePrice
                .multiply(BigDecimal.valueOf(PERCENT_FOR_MAX_FRANCHISE_PRICE))
                .divide(BigDecimal.valueOf(100));
        return franchisePrice.compareTo(maxLegalFranchisePrice) > 0;
    }

    public Automobile findAutomobileById(final long id) {
        log.info(String.format("Find automobile by id = %s", id));
        Optional<Automobile> automobileOptional = repository.findById(id);
        if (automobileOptional.isEmpty()) {
            String exceptionMessage = String.format("Automobile not found by id = %s", id);
            log.debug(exceptionMessage);
            throw new AutomobileNotFoundException(exceptionMessage);
        }
        return automobileOptional.get();
    }

    public Automobile deleteAutomobileById(final long id) {
        log.info(String.format("Delete automobile with id = %s", id));
        Optional<Automobile> automobileOptional = repository.findById(id);
        if (automobileOptional.isEmpty()) {
            log.debug(String.format("Automobile not found by id = %s", id));
            throw new AutomobileNotFoundException(String.format("Automobile not found by id = %s", id));
        }
        repository.deleteById(id);
        return automobileOptional.get();
    }
}
