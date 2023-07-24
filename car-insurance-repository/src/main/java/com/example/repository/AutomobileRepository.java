package com.example.repository;

import com.example.Automobile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AutomobileRepository extends PagingAndSortingRepository<Automobile, Long>, CrudRepository<Automobile, Long> {
    boolean existsByLicensePlate(String licensePlate);
}
