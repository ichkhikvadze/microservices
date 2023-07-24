package com.example.controller;

import com.example.domain.Person;
import com.example.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@Slf4j
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @Operation(summary = "Get all persons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all persons"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Iterable<Person> findAllPersons(
            @RequestParam(name = "page", required = false, defaultValue = "${page}") final int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "${page-size}") final int pageSize
    ) {
        log.info(String.format("Find %s persons on %s page", pageSize, page));
        return service.findAllPersons(page, pageSize);
    }

    @Operation(summary = "Create person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "409", description = "Person already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Person createPerson(@Parameter(description = "Person") @RequestBody @Valid Person person) {
        log.info(String.format("Create %s", person));
        return service.createPerson(person);
    }

    @Operation(summary = "Find person by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{id}")
    public Person findPersonById(@Parameter(description = "Person id") @PathVariable(name = "id") final long id) {
        log.info(String.format("Find person by id = %s", id));
        return service.findPersonById(id);
    }

    @Operation(summary = "Update person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public Person updatePerson(
            @Parameter(description = "Person id") @RequestParam final long id,
            @Parameter(description = "Person") @RequestBody @Valid final Person person) {
        log.info(String.format("Update person with id = %s to %s", id, person));
        return service.updatePerson(id, person);
    }

    @Operation(summary = "Delete person by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping
    public Person deletePersonById(@Parameter(description = "Person id") @RequestParam final long id) {
        log.info(String.format("Delete person with id = %s", id));
        return service.deletePersonById(id);
    }
}
