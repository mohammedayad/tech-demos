package com.ayad.holidaysservice.domain.model.dtos;

import java.util.List;

/**
 * Represents the result of country code validation.
 *
 * @param validCountryCodes   List of properly formatted country codes
 * @param invalidCountryCodes List of invalid/malformed country codes
 */
public record CountryCodeValidationResult(
        List<String> validCountryCodes,
        List<String> invalidCountryCodes
) {
}