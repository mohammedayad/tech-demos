package com.ayad.holidaysservice.common.utils;


import com.ayad.holidaysservice.domain.model.dtos.CountryCodeValidationResult;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Utility class containing holiday-related validation and calculation methods.
 * Provides shared constants and reusable functions.
 */
@Slf4j
@UtilityClass
public class HolidaysUtility {

    public final String SERVER_ERROR_MESSAGE = "Server Error";
    public final String SERVER_ERROR_CODE = "Server Error Code";
    public final String CONSTRAINT_VIOLATIONS = "CONSTRAINT_VIOLATIONS";

    public final String SERVICE_PROCESSING_ERROR_TITLE = "Service Processing Error";
    public final String UNKNOWN_COUNTRY_CODE = "Unknown Country Code: %s";

    public final String VALIDATION_FAILURE = "Validation failure for Country Code: %s and Year: %s";

    public final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";
    public final String ILLEGAL_ARGUMENT_ERROR_TITLE = "Illegal Argument Error";


    public final String COUNTRY_CODE_REGEX = "^[A-Za-z]{2}$";

    public final String COUNTRY_CODE_VALIDATION_ERROR_MESSAGE = "Country code must be 2 letters";


    /**
     * Validates country code format and returns partitioned results.
     *
     * @param codes List of country codes to validate
     * @return CountryCodeValidationResult with valid/invalid codes
     * @throws IllegalArgumentException if input is empty or all codes invalid
     */
    public CountryCodeValidationResult validateCountryCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            throw new IllegalArgumentException("Country codes cannot be null or empty");
        }

        Set<String> countryCodes = codes.stream()
                .map(String::toUpperCase)    // Convert to uppercase
                .collect(Collectors.toSet()); // Deduplicate

        Pattern countryCodePattern = Pattern.compile(COUNTRY_CODE_REGEX);

        // Partition codes into valid/invalid using grouping
        Map<Boolean, List<String>> groupedCodes = countryCodes.stream()
                .collect(Collectors.partitioningBy(
                        code -> countryCodePattern.matcher(code).matches()
                ));

        List<String> validCodes = groupedCodes.get(true);
        List<String> invalidCodes = groupedCodes.get(false);
        log.debug("valid country codes: {}", validCodes);
        log.debug("invalid country codes: {}", invalidCodes);
        if (validCodes.isEmpty()) { // no valid codes
            String errorMessage = String.format(
                    "All received country codes: %s are invalid. They must be 2-letter alphabetic strings.",
                    invalidCodes
            );
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (!invalidCodes.isEmpty()) {  // there invalid codes
            log.warn("Invalid country codes: {}. They must be 2-letter alphabetic strings.", invalidCodes);
        }

        return new CountryCodeValidationResult(validCodes, invalidCodes);
    }


    /**
     * Determines if a date falls on a weekend.
     *
     * @param date Date to check
     * @return true if date is Saturday or Sunday
     */
    public boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        boolean isWeekend = day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
        log.debug("is data {} {} a Weekend? {}", date, day, isWeekend);
        return isWeekend;
    }


}
