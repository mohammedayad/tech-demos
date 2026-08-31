package com.ayad.applicationlibrary.domain.repository.ifc;

import com.ayad.applicationlibrary.domain.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


/**
 * Repository for managing application entities.
 */
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    /**
     * Returns all applications available within the given
     * organization and school scope, including root applications.
     *
     * @param organizationId organization identifier
     * @param schoolId school identifier
     * @return matching applications
     */
    @Query("""
            SELECT a
            FROM Application a
            WHERE a.school.id = :schoolId
               OR a.organization.id = :organizationId
               OR a.level = com.ayad.applicationlibrary.domain.model.ApplicationLevel.ROOT
            """)
    List<Application> findAllApplicationsForOrganizationAndSchool(
            UUID organizationId,
            UUID schoolId);
}
