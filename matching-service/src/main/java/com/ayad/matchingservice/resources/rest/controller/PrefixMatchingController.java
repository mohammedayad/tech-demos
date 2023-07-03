package com.ayad.matchingservice.resources.rest.controller;


import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for prefix matching operations.
 */
@RestController
@RequestMapping("/api/prefix-matching/v1")
@Slf4j
public class PrefixMatchingController {

    private final PrefixMatchingService prefixMatchingService;

    public PrefixMatchingController(PrefixMatchingService prefixMatchingService) {
        this.prefixMatchingService = prefixMatchingService;
    }

    /**
     * Retrieves all the predefined prefixes.
     *
     * @return a list of predefined prefixes
     */
    @GetMapping("/prefixes")
    public ResponseEntity<List<String>> getAllPrefixes() {
        return new ResponseEntity<>(prefixMatchingService.getAllPrefixes(), HttpStatus.OK);
    }

    /**
     * Retrieves the longest prefix from a list of prefixes that matches the given input string.
     *
     * @param input the input string to search for the longest prefix match
     * @return a {@link ResponseEntity} object containing the longest prefix that matches the input string
     * @throws MatchingException if no prefix matching the input string is found
     */
    @GetMapping("/longestPrefix/{input}")
    public ResponseEntity<String> findLongestPrefix(@PathVariable String input) {
        log.debug("find the longest prefix for {}",input);
        return new ResponseEntity<>(prefixMatchingService.findLongestPrefix(input), HttpStatus.OK);
    }

    /**
     * Retrieves the longest prefix from a list of prefixes that matches the given input string.
     *
     * @param input the input string to search for the longest prefix match
     * @return a {@link ResponseEntity} object containing the longest prefix that matches the input string
     * @throws MatchingException if no prefix matching the input string is found
     */
    @GetMapping("/longestPrefix/DB/{input}")
    public ResponseEntity<String> findLongestPrefixUsingDB(@PathVariable String input) {
        log.debug("find the longest prefix for {}",input);
        return new ResponseEntity<>(prefixMatchingService.findLongestPrefixUsingDB(input), HttpStatus.OK);
    }
}
