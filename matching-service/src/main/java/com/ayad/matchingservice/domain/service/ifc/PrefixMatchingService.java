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
    /**
     * Builds the Trie by inserting each prefix from the provided list of prefixes.
     */
    void buildTrie(List<String> prefixes);

    /**
     * Retrieves the longest prefix match for the given input string.
     *
     * @return a string that contain the longest prefix
     */
    String findLongestPrefix(String input);
}
