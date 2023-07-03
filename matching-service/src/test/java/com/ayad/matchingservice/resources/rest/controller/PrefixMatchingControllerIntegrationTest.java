package com.ayad.matchingservice.resources.rest.controller;

/**
 * Integration tests for the {@link PrefixMatchingController} class.
 */

import com.ayad.matchingservice.common.exception.MatchingException;
import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;


/**
 * Integration tests for the {@link PrefixMatchingController} class.
 * H2 in-memory Database for storing the prefixes.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PrefixMatchingControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private static PrefixMatchingService prefixMatchingService;


    /**
     * Tests the {@link PrefixMatchingController#getAllPrefixes()} method by calling the REST API endpoint and verifying the response.
     * the prefixes are retrieved from H2 in-memory database
     */
    @Test
    public void testGetAllPrefixes() {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/api/prefix-matching/v1/prefixes", List.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat((List<String>)responseEntity.getBody(), hasItems("2y3fKTS", "4VdwEEXC8", "AWMa4vvOf"));

    }

    /**
     * Tests the {@link PrefixMatchingController#findLongestPrefix(String)} method by calling the REST API endpoint and verifying the response.
     */
    @Test
    public void testFindLongestPrefix() throws MatchingException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/prefix-matching/v1/longestPrefix/2y3fKTSfter", String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is("2y3fKTS"));
        ResponseEntity<String> responseEntity2 = restTemplate.getForEntity("/api/prefix-matching/v1/longestPrefix/KAWeqDO", String.class);
        assertThat(responseEntity2.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity2.getBody(), is("KAWeqDO"));
    }


    /**
     * Tests the case where the {@link PrefixMatchingController#findLongestPrefix(String)} method throws a {@link MatchingException} by calling the REST API endpoint and verifying the response.
     */
    @Test
    public void testFindLongestPrefixThrowsMatchingException() throws MatchingException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/prefix-matching/v1/longestPrefix/EEXC8ffffff", String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("No Prefix Matching has been found"));
    }
}
