package com.ayad.applicationlibrary.domain.repository.ifc;

import com.ayad.applicationlibrary.domain.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for managing organization entities.
 */
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
