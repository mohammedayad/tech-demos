package com.ayad.citysorter;

import com.ayad.citysorter.config.CitySorterConfig;
import com.ayad.citysorter.domain.service.ifc.CitySorterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class CitySorterApplication {


    private final CitySorterService citySorterService;
    private final CitySorterConfig citySorterConfig;

    public static void main(String[] args) {
        SpringApplication.run(CitySorterApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application is fully started. Running final startup tasks...");
//        citySorterService.sortCities(CitySorterUtility.CITIES_STORAGE_FOLDER);
        citySorterService.sortCities(citySorterConfig.getCitiesStorageFolder());

    }
}
