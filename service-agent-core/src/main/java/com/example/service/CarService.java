package com.example.service;

import com.example.Car;
import com.example.repository.CarRepository;
import com.example.Owner;
import com.example.repository.OwnerRepository;
import com.example.domain.Person;
import com.example.exception.*;
import com.example.facade.PersonFacade;
import com.example.mapper.PersonToOwnerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarService {

    private final CarRepository repository;
    private final OwnerRepository ownerRepository;
    private final PersonFacade personFacade;

    @Autowired
    public CarService(CarRepository repository, OwnerRepository ownerRepository, PersonFacade personFacade) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
        this.personFacade = personFacade;
    }

    public Iterable<Car> findAllCars(final int page, final int pageSize) {
        log.info(String.format("Find %s cars on %s page", pageSize, page));
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable).stream()
                .collect(Collectors.toList());
    }

    public Car createCar(Car car, final String personalNo) throws URISyntaxException {
        log.info(String.format("Create %s", car));
        Person person = personFacade.getPerson(personalNo)
                .orElseThrow(() -> {
                    String exceptionMessage = String.format("Person with personal no = %s not found", personalNo);
                    log.debug(exceptionMessage);
                    return new PersonNotFoundException(exceptionMessage);
                });
        Optional<Car> carOptional = repository.getCarByVinCode(car.getVinCode());
        if (!ownerRepository.existsByPersonalNo(personalNo)) {
            Owner newOwner = PersonToOwnerMapper.mapPersonToOwner(person);
            ownerRepository.save(newOwner);
        }
        Owner owner = ownerRepository.findByPersonalNo(personalNo).orElseThrow(() -> {
            String exceptionMessage = String.format("Owner with personal no = %s not found", personalNo);
            log.debug(exceptionMessage);
            return new OwnerNotFoundException(exceptionMessage);
        });
        if (carOptional.isPresent()) {
            Car existedCar = carOptional.get();
            existedCar.setOwner(owner);
            return repository.save(existedCar);
        }
        car.setOwner(owner);
        return repository.save(car);
    }

    public Car findCarById(final long id) {
        log.info(String.format("Find car by id = %s", id));
        Optional<Car> carOptional = repository.findById(id);
        if (carOptional.isEmpty()) {
            String exceptionMessage = String.format("Car not found by id = %s", id);
            log.debug(exceptionMessage);
            throw new CarNotFoundException(exceptionMessage);
        }
        return carOptional.get();
    }

    public Car getPersonCarByLicensePlate(String personalNo, String licensePlate) {
        log.info(String.format("Get car with license plate = %s of person with personal no = %s", licensePlate, personalNo));
        return repository.getPersonCarByLicensePlate(personalNo, licensePlate)
                .orElseThrow(() -> {
                    String exceptionMessage = String.format(
                            "Car with license plate = %s of person with personal no = %s not found",
                            licensePlate,
                            personalNo
                    );
                    log.debug(exceptionMessage);
                    return new CarNotFoundException(exceptionMessage);
                });
    }

    public Car updateCar(final long id, final Car car) {
        log.info(String.format("Update car with id = %s to %s", id, car));
        Optional<Car> existedCarOptional = repository.findById(id);
        if (existedCarOptional.isEmpty()) {
            String exceptionMessage = String.format("Car not found by id = %s", id);
            log.debug(exceptionMessage);
            throw new CarNotFoundException(exceptionMessage);
        }
        Car existedCar = existedCarOptional.get();
        if (existsCarWithSameLicensePlate(car.getLicensePlate(), existedCar.getLicensePlate())) {
            String exceptionMessage = String.format("Car with license plate = %s already exists", car.getLicensePlate());
            log.debug(exceptionMessage);
            throw new LicensePlateAlreadyExistsException(exceptionMessage);
        } else if (existsCarWithSameVinCode(car.getVinCode(), existedCar.getVinCode())) {
            String exceptionMessage = String.format("Car with vin code = %s already exists", car.getVinCode());
            log.debug(exceptionMessage);
            throw new VinCodeAlreadyExistsException(exceptionMessage);
        }
        car.setId(id);
        return repository.save(car);
    }

    private boolean existsCarWithSameVinCode(String newVinCode, String existingVinCode) {
        return repository.existsByVinCode(newVinCode) && !existingVinCode.equals(newVinCode);
    }

    private boolean existsCarWithSameLicensePlate(String newLicensePlate, String existingLicensePlate) {
        return repository.existsByLicensePlate(newLicensePlate) && !existingLicensePlate.equals(newLicensePlate);
    }

    public Car deleteCarById(final long id) {
        log.info(String.format("Delete car with id = %s", id));
        Optional<Car> carOptional = repository.findById(id);
        if (carOptional.isEmpty()) {
            String exceptionMessage = String.format("Car not found by id = %s", id);
            log.debug(exceptionMessage);
            throw new CarNotFoundException(exceptionMessage);
        }
        repository.deleteById(id);
        return carOptional.get();
    }
}
