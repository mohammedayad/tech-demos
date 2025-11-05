package com.ayad.citysorter.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Configuration properties for the City Sorter service.
 * Defines configurable properties related to city storage paths and output filenames.
 */
@Data
@Component
@ConfigurationProperties(prefix = "city-sorter")
public class CitySorterConfig {
    private String citiesStorageFolder = "classpath:cities-storage";
    private String citiesStorageOutputFileName = "city-sorter-output.txt";
}
