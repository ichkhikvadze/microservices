package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class RestTemplateConfig {

    @Value("${justice.ministry.host:localhost}")
    private String host;
    @Value("${justice.ministry.port:8080}")
    private Integer port;
    @Value("${justice.ministry.scheme:http}")
    private String scheme;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("personServiceURIBuilder")
    public UriComponentsBuilder personServiceURIBuilder() {
        return UriComponentsBuilder.newInstance()
                .host(host).port(port).scheme(scheme);
    }
}
