package com.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Component
@Slf4j
public class PersonFacade {

    private final RestTemplate restTemplate;
    private final UriComponentsBuilder uriComponentsBuilder;

    @Autowired
    public PersonFacade(RestTemplate restTemplate, @Qualifier("personServiceURIBuilder") UriComponentsBuilder uriComponentsBuilder) {
        this.restTemplate = restTemplate;
        this.uriComponentsBuilder = uriComponentsBuilder;
    }

    public Optional<Person> getPerson(String personalNo) throws URISyntaxException {
        log.info(String.format("Get person with personal no = %s from other service", personalNo));
        URI uri = new URI(uriComponentsBuilder.cloneBuilder().path(String.format("public/%s", personalNo)).toUriString());
        try {
            ResponseEntity<Person> personResponseEntity = restTemplate.getForEntity(uri, Person.class);
            return Optional.ofNullable(personResponseEntity.getBody());
        }catch (RestClientException ex){
            return Optional.empty();
        }
    }
}
