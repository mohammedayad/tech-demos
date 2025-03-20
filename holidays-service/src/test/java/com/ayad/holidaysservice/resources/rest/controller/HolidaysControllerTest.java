package com.ayad.holidaysservice.resources.rest.controller;

import com.ayad.holidaysservice.common.exceptions.ServiceProcessingException;
import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;
import com.ayad.holidaysservice.domain.service.ifc.HolidaysService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test suite for {@link HolidaysController}.
 * Tests controller layer in isolation with mocked service layer.
 * Verifies request mapping, parameter validation, and response handling.
 */
@WebMvcTest(HolidaysController.class)
class HolidaysControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private HolidaysService holidaysService;

    // Valid request for last celebrated holidays
    @Test
    void getLastCelebratedHolidays_Success() throws Exception {
        when(holidaysService.getLastCelebratedHolidays(5, "US"))
                .thenReturn(List.of(new PublicHoliday()));

        mockMvc.perform(get("/holidays-service/v1/api/last-celebrated-holidays/5/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // Invalid country code format
    @Test
    void getLastCelebratedHolidays_InvalidCountryCode() throws Exception {
        mockMvc.perform(get("/holidays-service/v1/api/last-celebrated-holidays/5/usa"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Illegal Argument Error"));
    }

    // Non-weekend holidays with mixed valid/invalid codes
    @Test
    void getNonWeekendHolidays_MixedCodes() throws Exception {
        when(holidaysService.getNonWeekendHolidayCount(2023, List.of("US", "XX")))
                .thenReturn(Map.of("US", 10L, "XX", -1L));

        mockMvc.perform(get("/holidays-service/v1/api/non-weekend-holidays/2023")
                        .param("countryCodes", "US", "XX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(10))
                .andExpect(jsonPath("$.XX").value(-1));
    }

    // Common holidays API failure simulation
    @Test
    void getCommonHolidays_ApiFailure() throws Exception {
        when(holidaysService.getCommonHolidays(2023, "US", "IN"))
                .thenThrow(new ServiceProcessingException(HttpStatus.NOT_FOUND,
                        "CONSTRAINT_VIOLATIONS", "Unknown Country Code: IN"));

        mockMvc.perform(get("/holidays-service/v1/api/common-holidays/2023")
                        .param("countryCode1", "US")
                        .param("countryCode2", "IN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Service Processing Error"));
    }
}
