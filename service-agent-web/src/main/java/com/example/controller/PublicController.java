package com.example.controller;

import com.example.Car;
import com.example.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private final CarService service;

    @Autowired
    public PublicController(CarService service) {
        this.service = service;
    }

    @Operation(summary = "Find car by license plate and personal no")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{personalNo}/{licensePlate}")
    public Car getPersonCarByLicensePlate(
            @PathVariable(name = "personalNo") String personalNo,
            @PathVariable(name = "licensePlate") String licensePlate) {
        log.info(String.format("Get car with license plate = %s of person with personal no = %s", licensePlate, personalNo));
        return service.getPersonCarByLicensePlate(personalNo, licensePlate);
    }
}
