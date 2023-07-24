package com.example.controller;

import com.example.Car;
import com.example.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/cars")
@Slf4j
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all cars"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Iterable<Car> findAllCars(
            @RequestParam(name = "page", required = false, defaultValue = "${page}") final int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "${page-size}") final int pageSize
    ) {
        log.info(String.format("Find %s cars on %s page", pageSize, page));
        return service.findAllCars(page, pageSize);
    }

    @Operation(summary = "Create car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "409", description = "Car already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/people/{personalNo}/vehicles")
    public Car createCar(
            @Parameter(description = "Car") @RequestBody @Valid Car car,
            @Parameter(description = "Personal No") @PathVariable(name = "personalNo") final String personalNo
    ) throws URISyntaxException {
        log.info(String.format("Create %s", car));
        return service.createCar(car, personalNo);
    }

    @Operation(summary = "Find car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{id}")
    public Car findCarById(@Parameter(description = "Car id") @PathVariable(name = "id") final long id) {
        log.info(String.format("Find car by id = %s", id));
        return service.findCarById(id);
    }

    @Operation(summary = "Update car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public Car updateCar(
            @Parameter(description = "Car id") @RequestParam final long id,
            @Parameter(description = "Car") @RequestBody @Valid final Car car) {
        log.info(String.format("Update car with id = %s to %s", id, car));
        return service.updateCar(id, car);
    }

    @Operation(summary = "Delete car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping
    public Car deleteCarById(@Parameter(description = "Car id") @RequestParam final long id) {
        log.info(String.format("Delete branch with id = %s", id));
        return service.deleteCarById(id);
    }
}
