package com.ayad.holidaysservice.clients.proxy.impl;

import com.ayad.holidaysservice.clients.proxy.ifc.HolidayApiClient;
import com.ayad.holidaysservice.common.exceptions.ServiceProcessingException;
import com.ayad.holidaysservice.common.utils.HolidaysUtility;
import com.ayad.holidaysservice.config.HolidayApiClientProperties;
import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Client implementation for interacting with the external holiday API.
 * Handles HTTP communication and error translation.
 */
@Slf4j
@Service
public class HolidayApiClientImpl implements HolidayApiClient {

    private final RestTemplate restTemplate;
    private final HolidayApiClientProperties apiClientProperties;

    public HolidayApiClientImpl(RestTemplate restTemplate, HolidayApiClientProperties apiClientProperties) {
        this.restTemplate = restTemplate;
        this.apiClientProperties = apiClientProperties;
    }


    /**
     * Retrieves public holidays from external API.
     *
     * @param countryCode ISO 2-letter country code
     * @param year        Target year
     * @return List of PublicHoliday objects
     * @throws ServiceProcessingException if API returns error status
     */
    @Override
    public List<PublicHoliday> getPublicHolidays(String countryCode, int year) {
        try {
            var publicHolidaysUrl = apiClientProperties.getUrl().expand(year, countryCode);
            log.info("Call Holidays API URL: {}", publicHolidaysUrl);
            ResponseEntity<List<PublicHoliday>> holidaysResponse = restTemplate.exchange(
                    publicHolidaysUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PublicHoliday>>() {
                    }
            );
            if (!HttpStatus.OK.equals(holidaysResponse.getStatusCode())) {
                log.error("Holidays API Client for {} Country Code returned status code: {}", countryCode, holidaysResponse.getStatusCode());
                throw new ServiceProcessingException(HttpStatus.NOT_FOUND,
                        HolidaysUtility.CONSTRAINT_VIOLATIONS,
                        String.format(HolidaysUtility.UNKNOWN_COUNTRY_CODE, countryCode));
            }
            List<PublicHoliday> holidays = holidaysResponse.getBody();
            log.info("Holidays API Client for {} Country Code returned {} Holidays with Response {}",
                    countryCode,
                    holidays.size(),
                    holidays);
            return holidays;
        } catch (HttpClientErrorException exception) {
            log.error("Error while calling holidays api:", exception);  // Error logging
            if (HttpStatus.NOT_FOUND.equals(exception.getStatusCode())) {
                throw new ServiceProcessingException(HttpStatus.NOT_FOUND,
                        HolidaysUtility.CONSTRAINT_VIOLATIONS,
                        String.format(HolidaysUtility.UNKNOWN_COUNTRY_CODE, countryCode));
            }
            throw new ServiceProcessingException(HttpStatus.BAD_REQUEST,
                    HolidaysUtility.CONSTRAINT_VIOLATIONS,
                    String.format(HolidaysUtility.VALIDATION_FAILURE, countryCode, year));


        }
    }
}
