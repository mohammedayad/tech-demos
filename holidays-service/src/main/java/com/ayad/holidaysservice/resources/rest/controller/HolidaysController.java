package com.ayad.holidaysservice.resources.rest.controller;


import com.ayad.holidaysservice.common.utils.HolidaysUtility;
import com.ayad.holidaysservice.domain.model.dtos.ProblemDto;
import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;
import com.ayad.holidaysservice.domain.service.ifc.HolidaysService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * REST controller for handling holiday-related operations.
 * Provides endpoints for retrieving various types of holiday information.
 */
@RestController
@RequestMapping("/holidays-service/v1/api")
@Tag(name = "Holidays APIs", description = "API for managing Holidays")
@Slf4j
public class HolidaysController {

    private final HolidaysService holidaysService;

    public HolidaysController(HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }


    /**
     * Retrieves the last celebrated holidays for a specific country.
     *
     * @param numberOfHolidays Number of recent holidays to retrieve (must be ≥ 1)
     * @param countryCode      ISO 2-letter country code (pattern: ^[A-Za-z]{2}$)
     * @return List of PublicHoliday objects with status 200, or error response
     */
    @GetMapping("/last-celebrated-holidays/{numberOfHolidays}/{countryCode}")
    @Operation(summary = "Get last celebrated number of holidays ", description = "Retrieves last celebrated holidays by number of holidays and country code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns the requested last celebrated number of holidays"),
            @ApiResponse(responseCode = "400", description = "Validation failure",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "404", description = "CountryCode is unknown",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<List<PublicHoliday>> getLastCelebratedHolidays(@PathVariable @Min(1) long numberOfHolidays,
                                                                         @PathVariable @Pattern(regexp = HolidaysUtility.COUNTRY_CODE_REGEX,
                                                                                 message = HolidaysUtility.COUNTRY_CODE_VALIDATION_ERROR_MESSAGE)
                                                                         String countryCode) {
        log.info("Get last {} celebrated holidays in {} country",
                numberOfHolidays, countryCode);
        return new ResponseEntity<>(
                holidaysService.getLastCelebratedHolidays(numberOfHolidays, countryCode),
                HttpStatus.OK);
    }


    /**
     * Calculates non-weekend holidays count for multiple countries in a given year.
     *
     * @param year         The year to check (must be ≥ 1975)
     * @param countryCodes List of ISO 2-letter country codes
     * @return Map of country codes to holiday counts with status 200, or error response
     */
    @GetMapping("/non-weekend-holidays/{year}")
    @Operation(summary = "Get Non Weekend Holidays ", description = "Retrieves Non Weekend Holidays for specific Year and specific Country Codes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns the requested Non Weekend Holidays."),
            @ApiResponse(responseCode = "400", description = "Validation failure",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<Map<String, Long>> getNonWeekendHolidays(@PathVariable @Min(1975) int year,
                                                                   @RequestParam List<String> countryCodes) {
        log.info("Get Non Weekend Holidays for Year {} and Country Codes {}",
                year, countryCodes);
        return new ResponseEntity<>(
                holidaysService.getNonWeekendHolidayCount(year, countryCodes),
                HttpStatus.OK
        );

    }


    /**
     * Finds common holidays between two countries in a specific year.
     *
     * @param year         The year to check (must be ≥ 1975)
     * @param countryCode1 First ISO 2-letter country code
     * @param countryCode2 Second ISO 2-letter country code
     * @return List of common PublicHoliday objects with status 200, or error response
     */
    @GetMapping("/common-holidays/{year}")
    @Operation(summary = "Get Common Holidays ", description = "Retrieves the Common Holidays between 2 Country Codes for specific Year.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns the requested Common Holidays."),
            @ApiResponse(responseCode = "400", description = "Validation failure",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "404", description = "CountryCode is unknown",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<List<PublicHoliday>> getCommonHolidays(@PathVariable @Min(1975) int year,
                                                                 @RequestParam @Pattern(regexp = HolidaysUtility.COUNTRY_CODE_REGEX,
                                                                         message = HolidaysUtility.COUNTRY_CODE_VALIDATION_ERROR_MESSAGE) String countryCode1,
                                                                 @RequestParam @Pattern(regexp = HolidaysUtility.COUNTRY_CODE_REGEX,
                                                                         message = HolidaysUtility.COUNTRY_CODE_VALIDATION_ERROR_MESSAGE) String countryCode2) {

        log.info("Get Common Holidays between country codes: {} and {} in Year {}",
                countryCode1, countryCode2, year);
        return new ResponseEntity<>(
                holidaysService.getCommonHolidays(year, countryCode1, countryCode2),
                HttpStatus.OK
        );


    }
}
