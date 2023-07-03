package com.ayad.matchingservice.domain.repository.ifc;

import com.ayad.matchingservice.domain.model.PrefixEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for the {@link PrefixRepository} class.
 */
@SpringBootTest
@Transactional
public class PrefixRepositoryIntegrationTest {
    @Autowired
    private PrefixRepository prefixRepository;
    /**
     * Tests the {@link PrefixRepository#findByMatchingPrefix(String)} method with a matching input string.
     *It then calls the {@link PrefixRepository#findByMatchingPrefix(String)} method with an
     * input string of "KAWeqDO", which matches both prefixes. It verifies that the actual result contains both
     * {@link PrefixEntity} instances, and that they are sorted in descending order of prefix length.
     */
    @Test
    void testFindByMatchingPrefix() {
        // execute
        Optional<List<PrefixEntity>> prefixEntities = prefixRepository.findByMatchingPrefix("KAWeqDO");

        // verify
        assertTrue(prefixEntities.isPresent());
        assertEquals(2, prefixEntities.get().size());
        assertEquals("KAWeqDO", prefixEntities.get().get(0).getPrefix());// longest match
        assertEquals("KAWeqD", prefixEntities.get().get(1).getPrefix());
    }
}
