package com.ayad.holidaysservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for REST template setup.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean for HTTP communication.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
