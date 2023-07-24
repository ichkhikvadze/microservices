package com.example.repository;

import com.example.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long>, CrudRepository<Image, Long> {
    @Query("SELECT i FROM Image i " +
            "WHERE i.automobile.id = :id")
    List<Image> findImagesByAutomobileId(long id);
}
