package com.ayad.matchingservice.domain.repository.ifc;

import com.ayad.matchingservice.domain.model.PrefixEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing prefix entities.
 */
@Repository
public interface PrefixRepository extends JpaRepository<PrefixEntity, Long> {
}
