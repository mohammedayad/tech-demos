package com.ayad.matchingservice.resources.rest.controller;


import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for prefix matching operations.
 */
@RestController
@RequestMapping("/api/prefix-matching/v1")
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
}
