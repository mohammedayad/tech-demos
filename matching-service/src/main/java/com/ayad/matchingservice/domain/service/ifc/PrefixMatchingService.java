package com.ayad.matchingservice.domain.service.ifc;

import java.util.List;

/**
 * Service class for performing prefix matching operations.
 */
public interface PrefixMatchingService {

    /**
     * Retrieves all the predefined prefixes.
     *
     * @return a list of predefined prefixes
     */
    List<String> getAllPrefixes();
}
