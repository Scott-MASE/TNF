package com.tus.uiandrest.controller;

import com.tus.uiandrest.repositories.AnomalyRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class AIControllerTestConfig {

    @Bean
    public AnomalyRepository anomalyRepository() {
        return Mockito.mock(AnomalyRepository.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }
}
