package com.example;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CarRepository extends PagingAndSortingRepository<Car, Long>, CrudRepository<Car, Long> {
    Optional<Car> getCarByVinCode(String vinCode);
    @Query("SELECT c FROM Car c WHERE c.owner.personalNo = :personalNo AND c.licensePlate = :licensePlate")
    Optional<Car> getPersonCarByLicensePlate(String personalNo, String licensePlate);
    boolean existsByLicensePlate(String licensePlate);
    boolean existsByVinCode(String vinCode);
}
