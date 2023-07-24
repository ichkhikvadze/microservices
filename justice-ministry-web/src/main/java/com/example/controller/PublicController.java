package com.example.controller;

import com.example.domain.Person;
import com.example.service.PersonService;
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

    private final PersonService service;

    @Autowired
    public PublicController(PersonService service) {
        this.service = service;
    }

    @Operation(summary = "Find person by personalNo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{personalNo}")
    public Person getPersonByPersonalNo(@PathVariable(name = "personalNo") final String personalNo) {
        log.info(String.format("Find person by personalNo = %s", personalNo));
        return service.findPersonByPersonalNo(personalNo);
    }
}
