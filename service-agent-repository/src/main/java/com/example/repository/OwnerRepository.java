package com.example.repository;

import com.example.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OwnerRepository extends PagingAndSortingRepository<Owner, Long>, CrudRepository<Owner, Long> {
    boolean existsByPersonalNo(String personalNo);
    Optional<Owner> findByPersonalNo(String personalNo);
}
