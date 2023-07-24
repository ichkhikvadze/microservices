package com.example.facade;

import com.example.Car;
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
public class CarFacade {

    private final RestTemplate restTemplate;
    private final UriComponentsBuilder uriComponentsBuilder;

    public CarFacade(RestTemplate restTemplate, @Qualifier("carServiceUriBuilder") UriComponentsBuilder uriComponentsBuilder) {
        this.restTemplate = restTemplate;
        this.uriComponentsBuilder = uriComponentsBuilder;
    }

    public Optional<Car> getCar(String personalNo, String licensePlate) throws URISyntaxException {
        String uriPath = String.format("public/%s/%s", personalNo, licensePlate);
        URI uri = new URI(uriComponentsBuilder.cloneBuilder().path(uriPath).toUriString());
        try {
            ResponseEntity<Car> carResponseEntity = restTemplate.getForEntity(uri, Car.class);
            return Optional.ofNullable(carResponseEntity.getBody());
        }catch (RestClientException ex) {
            return Optional.empty();
        }
    }
}
