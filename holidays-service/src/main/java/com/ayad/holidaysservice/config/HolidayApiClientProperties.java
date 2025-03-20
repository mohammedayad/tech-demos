package com.ayad.holidaysservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriTemplate;

/**
 * Configuration properties for holiday API client.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "holiday-api-client")
public class HolidayApiClientProperties {
    private UriTemplate url;
    private int year;
}
