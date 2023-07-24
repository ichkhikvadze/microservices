package com.example.repository;

import com.example.Insured;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface InsuredRepository extends PagingAndSortingRepository<Insured, Long>, CrudRepository<Insured, Long> {
    Optional<Insured> findByPersonalNo(String personalNo);
    boolean existsByPersonalNo(String personalNo);
}
