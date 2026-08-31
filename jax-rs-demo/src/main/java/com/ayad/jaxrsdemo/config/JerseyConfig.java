package com.ayad.jaxrsdemo.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig {
    @Bean
    public ResourceConfig resourceConfig() {
        return new ResourceConfig().packages("com.ayad.jaxrsdemo.controller");
    }
}
