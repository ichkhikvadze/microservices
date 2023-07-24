package com.example.controller;

import com.example.Automobile;
import com.example.enums.InsuranceType;
import com.example.service.AutomobileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/automobiles")
@Slf4j
public class AutomobileController {

    private final AutomobileService service;

    @Autowired
    public AutomobileController(AutomobileService service) {
        this.service = service;
    }

    @Operation(summary = "Get all automobiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all automobiles"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Iterable<Automobile> findAllAutomobiles(
            @RequestParam(name = "page", required = false, defaultValue = "${page}") final int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "${page-size}") final int pageSize
    ) {
        log.info(String.format("Find %s automobiles on %s page", pageSize, page));
        return service.findAllAutomobiles(page, pageSize);
    }

    @Operation(summary = "Create automobile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Automobile created"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "409", description = "Automobile already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Automobile createAutomobile(
            @RequestParam final String licensePlate,
            @RequestParam final String personalNo,
            @RequestParam final BigDecimal franchisePrice,
            @RequestParam final BigDecimal fullInsurancePrice,
            @RequestParam final String insuranceType
    ) throws URISyntaxException {
        log.info(String.format("Create car with license plate = %s of person with personal no = %s", licensePlate, personalNo));
        InsuranceType type = InsuranceType.valueOf(insuranceType.toUpperCase());
        return service.createAutomobile(licensePlate, personalNo, franchisePrice, fullInsurancePrice, type);
    }

    @Operation(summary = "Find automobile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Automobile found"),
            @ApiResponse(responseCode = "404", description = "Automobile not found"),
            @ApiResponse(responseCode = "406", description = "Illegal insurance criteria")
    })
    @GetMapping("/{id}")
    public Automobile findAutomobileById(@Parameter(description = "Automobile id") @PathVariable(name = "id") final long id) {
        log.info(String.format("Find automobile by id = %s", id));
        return service.findAutomobileById(id);
    }

    @Operation(summary = "Delete automobile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Automobile deleted"),
            @ApiResponse(responseCode = "404", description = "Automobile not found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping
    public Automobile deleteAutomobileById(@Parameter(description = "Automobile id") @RequestParam final long id) {
        log.info(String.format("Delete automobile with id = %s", id));
        return service.deleteAutomobileById(id);
    }
}
