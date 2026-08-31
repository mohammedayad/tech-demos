package com.ayad.applicationlibrary.domain.repository.ifc;

import com.ayad.applicationlibrary.domain.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


/**
 * Repository for managing school entities.
 */
public interface SchoolRepository extends JpaRepository<School, UUID> {
}
