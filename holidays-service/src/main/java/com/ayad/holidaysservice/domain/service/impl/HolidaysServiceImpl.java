package com.ayad.holidaysservice.domain.service.impl;

import com.ayad.holidaysservice.clients.proxy.ifc.HolidayApiClient;

import com.ayad.holidaysservice.common.exceptions.ServiceProcessingException;
import com.ayad.holidaysservice.common.utils.HolidaysUtility;
import com.ayad.holidaysservice.config.HolidayApiClientProperties;
import com.ayad.holidaysservice.domain.model.dtos.CountryCodeValidationResult;
import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;
import com.ayad.holidaysservice.domain.service.ifc.HolidaysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Service implementation for holiday-related business logic.
 * Handles data processing, external API integration, and concurrency management.
 */
@Slf4j
@Service
public class HolidaysServiceImpl implements HolidaysService {


    private final HolidayApiClient holidayApiClient;
    private final HolidayApiClientProperties apiClientProperties;

    private final ExecutorService executor;

    public HolidaysServiceImpl(HolidayApiClient holidayApiClient,
                               HolidayApiClientProperties apiClientProperties,
                               @Qualifier("asyncExecutor") ExecutorService executor) {
        this.holidayApiClient = holidayApiClient;
        this.apiClientProperties = apiClientProperties;
        this.executor = executor;
    }

    @Override
    public List<PublicHoliday> getLastCelebratedHolidays(long numberOfHolidays, String countryCode) {
        log.info("Get last {} celebrated holidays in {} and {} country",
                numberOfHolidays, apiClientProperties.getYear(), countryCode);
        List<PublicHoliday> holidays = holidayApiClient.getPublicHolidays(countryCode, apiClientProperties.getYear());
        List<PublicHoliday> filteredHolidays = holidays.stream()
                .filter(publicHoliday -> publicHoliday.getDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(PublicHoliday::getDate).reversed())
                .limit(numberOfHolidays)
                .toList();
        log.info("last {} celebrated holidays are {}", numberOfHolidays, filteredHolidays);
        return filteredHolidays;
    }


    /**
     * Retrieves and processes non-weekend holiday counts using parallel execution.
     *
     * @param year         Target year for holiday data
     * @param countryCodes List of country codes to process
     * @return Map containing country codes and their non-weekend holiday counts
     * @throws IllegalArgumentException if input validation fails
     */
    @Override
    public Map<String, Long> getNonWeekendHolidayCount(int year, List<String> countryCodes) {
        log.debug("Start Getting Non Weekend Holidays for country codes: {} in year {} ", countryCodes, year);
        CountryCodeValidationResult countryCodeValidationResult = HolidaysUtility.validateCountryCodes(countryCodes);
        List<CompletableFuture<Map.Entry<String, Long>>> futures = countryCodeValidationResult.validCountryCodes().stream()
                .map(code -> CompletableFuture.supplyAsync(() -> {
                            try {
                                List<PublicHoliday> holidays = holidayApiClient.getPublicHolidays(code, year);
                                long count = holidays.stream()
                                        .filter(h -> !HolidaysUtility.isWeekend(h.getDate()))
                                        .count();
                                log.info("Country Code: {} has {} Non Weekend Holidays", code, count);
                                return Map.entry(code, count);
                            } catch (ServiceProcessingException e) {
                                log.error("Error processing {}: {}", code, e.getMessage());
                                return Map.entry(code, -1L);
                            }
                        }, executor)
                        .completeOnTimeout(Map.entry(code, -1L), 10, TimeUnit.SECONDS))
                .toList();

        Map<String, Long> holidaysResult = futures.stream()
                .map(CompletableFuture::join)
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Handle duplicates
                        LinkedHashMap::new
                ));
        // add in valid codes with -1 as value
        if (!countryCodeValidationResult.invalidCountryCodes().isEmpty()) {
            log.debug("add non valid country codes {} with -1 as value", countryCodeValidationResult.invalidCountryCodes());
            Map<String, Long> invalidCodesHolidaysCount = countryCodeValidationResult.invalidCountryCodes().stream()
                    .collect(Collectors.toMap(
                            key -> key,         // Key mapper
                            key -> -1L,          // Value mapper
                            (existing, replacement) -> existing, // Merge function for duplicates
                            LinkedHashMap::new  // Preserve insertion order (optional)
                    ));
            holidaysResult.putAll(invalidCodesHolidaysCount);
        }
        log.info("Non Weekend Holidays Result: {}", holidaysResult);
        return holidaysResult;
    }


    /**
     * Finds common holidays between two countries using parallel API calls.
     *
     * @param year         Target year for comparison
     * @param countryCode1 First country code
     * @param countryCode2 Second country code
     * @return List of common holidays sorted by date
     * @throws ServiceProcessingException if API communication fails
     */
    @Override
    public List<PublicHoliday> getCommonHolidays(int year,
                                                 String countryCode1,
                                                 String countryCode2) {
        log.debug("Start Getting common Holidays between country codes: {} and {} in Year {} ",
                countryCode1,
                countryCode2,
                year);

        // Execute API calls asynchronously
        CompletableFuture<List<PublicHoliday>> future1 = CompletableFuture.supplyAsync(
                () -> holidayApiClient.getPublicHolidays(countryCode1, year),
                executor
        );

        CompletableFuture<List<PublicHoliday>> future2 = CompletableFuture.supplyAsync(
                () -> holidayApiClient.getPublicHolidays(countryCode2, year),
                executor
        );

        // Combine results when both complete
        List<PublicHoliday> commonHolidays = future1.thenCombine(future2, (firstHolidays, secondHolidays) -> {
            Set<LocalDate> secondDates = secondHolidays.stream()
                    .map(PublicHoliday::getDate)
                    .collect(Collectors.toSet());

            return firstHolidays.stream()
                    .filter(h1 -> secondDates.contains(h1.getDate()))
                    .distinct()
                    .toList();
        }).join(); // Block to get the final result (or return a CompletableFuture)

        log.info("Common Holidays between country codes: {} and {} in Year {} size: {} and result: {}",
                countryCode1,
                countryCode2,
                year,
                commonHolidays.size(),
                commonHolidays);
        return commonHolidays;

    }
}
