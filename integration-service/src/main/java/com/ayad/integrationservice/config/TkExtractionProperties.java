package com.ayad.integrationservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tk-extraction")
public class TkExtractionProperties {

    private String url;
    private String account;
    private String username;
    private String password;
}
