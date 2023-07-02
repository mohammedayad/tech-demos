package com.ayad.matchingservice.domain.service.impl;

import com.ayad.matchingservice.common.exception.MatchingException;
import com.ayad.matchingservice.common.utils.MatchingUtility;
import com.ayad.matchingservice.domain.model.PrefixEntity;
import com.ayad.matchingservice.domain.repository.ifc.PrefixRepository;
import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PrefixMatchingServiceImplTest {
    @Mock
    private PrefixRepository prefixRepository;

    private PrefixMatchingService prefixMatchingService;

    @BeforeEach
    public void setUp() {
        prefixMatchingService = new PrefixMatchingServiceImpl(prefixRepository);
    }

    /**
     * Tests the {@link PrefixMatchingService#getAllPrefixes()} method to ensure that it correctly retrieves all prefixes from the repository.
     */
    @Test
    public void testGetAllPrefixes() {
        List<PrefixEntity> prefixEntities = new ArrayList<>();
        PrefixEntity prefixEntity1=new PrefixEntity();
        prefixEntity1.setId(1l);
        prefixEntity1.setPrefix("abc");
        PrefixEntity prefixEntity2=new PrefixEntity();
        prefixEntity2.setId(2l);
        prefixEntity2.setPrefix("def");
        prefixEntities.add(prefixEntity1);
        prefixEntities.add(prefixEntity2);
        when(prefixRepository.findAll()).thenReturn(prefixEntities);

        List<String> prefixes = prefixMatchingService.getAllPrefixes();

        assertThat(prefixes.size(), is(2));
        assertThat(prefixes, containsInAnyOrder("abc", "def"));
    }

    /**
     * Tests the {@link PrefixMatchingService#buildTrie(List)} method to ensure that it correctly builds a prefix trie.
     */
    @Test
    public void testBuildTrie() {
        // Set up the test data
        List<String> prefixes = Arrays.asList("abc", "def", "ghij");

        // Call the method being tested
        prefixMatchingService.buildTrie(prefixes);

        // Verify the results
        assertThat(prefixMatchingService.findLongestPrefix("abcdef"), is("abc"));
        assertThat(prefixMatchingService.findLongestPrefix("defhi"), is("def"));
        assertThat(prefixMatchingService.findLongestPrefix("abc"), is("abc"));
        assertThat(prefixMatchingService.findLongestPrefix("def"), is("def"));
        assertThat(prefixMatchingService.findLongestPrefix("ghij"), is("ghij"));
    }

    /**
     * Tests the {@link PrefixMatchingService#findLongestPrefix(String)} method to ensure that it correctly finds the longest prefix that matches a given input string.
     *
     * @throws MatchingException if no prefix matching the input string is found
     */
    @Test
    public void testFindLongestPrefix() throws MatchingException {
        // Set up the test data
        List<String> prefixes = Arrays.asList("abc", "def", "ghij");
        prefixMatchingService.buildTrie(prefixes);

        // Call the method being tested
        String longestPrefix = prefixMatchingService.findLongestPrefix("abctest");
        // Verify the results
        assertThat(longestPrefix, is("abc"));
        // Test for an input string with no matching prefix
        try {
            prefixMatchingService.findLongestPrefix("EEXCabcffffdef");
            fail("Expected MatchingException to be thrown");
        } catch (MatchingException e) {
            assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getErrorCode(), is(MatchingUtility.CONSTRAINT_VIOLATIONS));
            assertThat(e.getErrorMessage(), is(MatchingUtility.NO_PREFIX_MATCHING_FOUND));
        }

    }
}
