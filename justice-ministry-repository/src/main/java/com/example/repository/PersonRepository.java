package com.example.repository;

import com.example.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long>, CrudRepository<Person, Long> {
    boolean existsPersonByPersonalNo(String personalNo);
    Optional<Person> findPersonByPersonalNo(String personalNo);
}
