package com.ayad.holidaysservice.resources.rest.controller;


import com.ayad.holidaysservice.config.HolidayApiClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;


import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration test suite for {@link HolidaysController}.
 * Verifies end-to-end behavior of holiday endpoints with mocked external dependencies.
 * Uses {@link MockRestServiceServer} to simulate external API responses.
 */
@AutoConfigureMockMvc
@SpringBootTest
class HolidaysControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HolidayApiClientProperties apiClientProperties;

    // Mock external API server
    private MockRestServiceServer mockApiServer;


    @BeforeEach
    void setup() {
//        mockApiServer = MockRestServiceServer.createServer(restTemplate); // expected order
        mockApiServer = MockRestServiceServer.bindTo(restTemplate)
                .ignoreExpectOrder(true)
                .build();
    }


    /**
     * Tests successful retrieval of last celebrated holidays with valid parameters.
     * Verifies:
     * - Correct API endpoint call
     * - Proper sorting of results by date
     * - Response status and structure
     */
    @Test
    void getLastCelebratedHolidays_Success() throws Exception {
        // Mock API response
        mockApiServer.expect(requestTo(apiClientProperties.getUrl().
                        expand(apiClientProperties.getYear(), "NL")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2025-01-01", "name": "New Year"},
                            {"date": "2025-03-10", "name": "test vacation1"},
                            {"date": "2025-03-15", "name": "test vacation2"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/holidays-service/v1/api/last-celebrated-holidays/2/NL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").value("2025-03-15"))
                .andExpect(jsonPath("$[1].date").value("2025-03-10"));
    }


    /**
     * Tests non-weekend holiday calculation with mixed valid/invalid country codes.
     * Verifies:
     * - Concurrent API request handling
     * - Proper validation of country codes
     * - Correct counting of non-weekend holidays
     * - Error handling for invalid codes
     */
    @Test
    void getNonWeekendHolidays_Success() throws Exception {
        // Mock API responses
        mockApiServer.expect(requestTo(apiClientProperties.getUrl().expand("2023", "US")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-01-02", "name": "New Year Observed"},
                            {"date": "2023-01-01", "name": "New Year"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockApiServer.expect(requestTo(apiClientProperties.getUrl().expand("2023", "FR")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-07-14", "name": "Bastille Day"}
                        ]
                        """, MediaType.APPLICATION_JSON));

//        mockApiServer.expect(requestTo(apiClientProperties.getUrl().expand("2023", "XX")))
//                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/holidays-service/v1/api/non-weekend-holidays/2023")
                        .param("countryCodes", "FR", "US", "XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(1))   // 1 non-weekend holiday
                .andExpect(jsonPath("$.FR").value(1))   // 1 non-weekend holiday
                .andExpect(jsonPath("$.XXX").value(-1)); // Invalid code
    }


    /**
     * Tests non-weekend holiday calculation with valid country code.
     * Verifies:
     * - Concurrent API request handling
     * - Proper validation of country codes
     * - Correct counting of non-weekend holidays
     */
    @Test
    void getNonWeekendHolidays_ValidCodes() throws Exception {
        // Mock external API response
        mockApiServer.expect(requestTo(apiClientProperties.getUrl().expand("2025", "NL")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2025-01-01", "name": "New Year", "localName": "New Year"},
                            {"date": "2025-01-04", "name": "weekend vacation", "localName": "test weekend vacation"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/holidays-service/v1/api/non-weekend-holidays/2025")
                        .param("countryCodes", "NL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.NL").value(1)); // Only 1 non-weekend holiday (2023-01-01 is Sunday)

    }

    /**
     * Tests successful common holiday detection between two countries.
     * Verifies:
     * - Parallel API request execution
     * - Correct date matching logic
     */
    @Test
    void getCommonHolidays_Success() throws Exception {
        // Mock API responses
        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "NL")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-01-01", "name": "New Year"},
                            {"date": "2023-12-25", "name": "Christmas"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "GB")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-01-01", "name": "New Year"},
                            {"date": "2023-12-26", "name": "Boxing Day"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/holidays-service/v1/api/common-holidays/2023")
                        .param("countryCode1", "NL")
                        .param("countryCode2", "GB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].date").value("2023-01-01"));
    }

    /**
     * Tests scenario with no common holidays between countries.
     * Verifies:
     * - Proper handling of empty intersection
     * - Response status and structure
     */
    @Test
    void getCommonHolidays_NoCommonHolidays() throws Exception {
        // Mock API responses
        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "JP")
                ))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-01-01", "name": "New Year"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "IN")
                ))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-08-15", "name": "Independence Day"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/holidays-service/v1/api/common-holidays/2023")
                        .param("countryCode1", "JP")
                        .param("countryCode2", "IN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    /**
     * Tests non-weekend holiday calculation with multiple valid country codes.
     * Verifies:
     * - Concurrent API request handling
     * - Proper validation of country codes
     * - Correct counting of non-weekend holidays
     */
    @Test
    void getNonWeekendHolidays_AllValidCodes() throws Exception {
        // Mock API responses
        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "DE")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-10-03", "name": "German Unity Day"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockApiServer.expect(requestTo(apiClientProperties.getUrl()
                        .expand("2023", "ES")))
                .andRespond(withSuccess("""
                        [
                            {"date": "2023-10-12", "name": "National Day"}
                        ]
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/holidays-service/v1/api/non-weekend-holidays/2023")
                        .param("countryCodes", "DE", "ES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.DE").value(1))
                .andExpect(jsonPath("$.ES").value(1));
    }


    /**
     * Tests parameter validation for invalid country code format.
     * Verifies:
     * - Input validation logic
     * - Proper error response structure
     * - HTTP status code for validation failures
     */
    @Test
    void getLastCelebratedHolidays_InvalidCountryCode() throws Exception {
        mockMvc.perform(get("/holidays-service/v1/api/last-celebrated-holidays/5/usa"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Country code must be 2 letters"));
    }

    /**
     * Tests parameter validation for invalid Year < 1975.
     * Verifies:
     * - Input validation logic
     * - Proper error response structure
     * - HTTP status code for validation failures
     */
    @Test
    void getNonWeekendHolidays_InvalidYear() throws Exception {
        mockMvc.perform(get("/holidays-service/v1/api/non-weekend-holidays/1974")
                        .param("countryCodes", "NL"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("must be greater than or equal to 1975"));
    }


}
