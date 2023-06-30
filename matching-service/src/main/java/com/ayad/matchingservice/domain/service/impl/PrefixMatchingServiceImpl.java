package com.ayad.matchingservice.domain.service.impl;

import com.ayad.matchingservice.domain.model.PrefixEntity;
import com.ayad.matchingservice.domain.repository.ifc.PrefixRepository;
import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the PrefixMatchingService interface.
 */
@Service
public class PrefixMatchingServiceImpl implements PrefixMatchingService {

    private final PrefixRepository prefixRepository;

    public PrefixMatchingServiceImpl(PrefixRepository prefixRepository) {
        this.prefixRepository = prefixRepository;
    }

    /**
     * Retrieves all the prefix values.
     *
     * @return a list of prefix values
     */
    @Override
    public List<String> getAllPrefixes() {
        List<PrefixEntity> prefixEntities = prefixRepository.findAll();
        return prefixEntities.stream().map(PrefixEntity::getPrefix).collect(Collectors.toList());
    }
}

