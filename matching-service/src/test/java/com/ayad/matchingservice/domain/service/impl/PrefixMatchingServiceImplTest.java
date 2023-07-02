package com.ayad.matchingservice.domain.service.impl;

import com.ayad.matchingservice.domain.model.PrefixEntity;
import com.ayad.matchingservice.domain.repository.ifc.PrefixRepository;
import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
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
}
