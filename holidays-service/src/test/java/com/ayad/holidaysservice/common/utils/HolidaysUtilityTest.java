package com.ayad.holidaysservice.common.utils;

import com.ayad.holidaysservice.domain.model.dtos.CountryCodeValidationResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test suite for {@link HolidaysUtility}.
 * Tests validation logic and utility functions.
 * Verifies country code validation and date calculations.
 */
class HolidaysUtilityTest {

    // Valid country code validation
    @Test
    void validateCountryCodes_ValidCodes() {
        CountryCodeValidationResult result =
                HolidaysUtility.validateCountryCodes(List.of("US", "FR"));

        assertThat(result.validCountryCodes()).containsExactlyInAnyOrder("US", "FR");
        assertTrue(result.invalidCountryCodes().isEmpty());
    }

    // Mixed valid/invalid codes
    @Test
    void validateCountryCodes_MixedCodes() {
        CountryCodeValidationResult result =
                HolidaysUtility.validateCountryCodes(List.of("US", "12", "Fr"));

        assertThat(result.validCountryCodes()).containsExactlyInAnyOrder("US", "FR");
        assertEquals(List.of("12"), result.invalidCountryCodes());
    }

    //  Empty input handling
    @Test
    void validateCountryCodes_EmptyList() {
        assertThrows(IllegalArgumentException.class, () -> {
            HolidaysUtility.validateCountryCodes(List.of());
        });
    }

    //  Weekend detection logic
    @Test
    void isWeekend_SaturdayReturnsTrue() {
        assertTrue(HolidaysUtility.isWeekend(LocalDate.of(2025, 3, 15))); // Saturday
    }
}
