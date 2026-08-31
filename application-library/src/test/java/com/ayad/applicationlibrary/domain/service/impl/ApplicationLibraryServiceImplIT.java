package com.ayad.applicationlibrary.domain.service.impl;


import com.ayad.applicationlibrary.domain.model.Application;
import com.ayad.applicationlibrary.domain.service.ifc.ApplicationLibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.ayad.applicationlibrary.common.utils.TestUtils.assertApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for {@link ApplicationLibraryService}.
 */
@SpringBootTest
class ApplicationLibraryServiceImplIT {

    @Autowired
    private ApplicationLibraryService applicationLibraryService;


    /**
     * Verifies that school-level applications override organization
     * and root-level applications.
     */
    @Test
    void shouldReturnApplicationsForCloudCollegeStudent() {

        List<Application> applications =
                applicationLibraryService.getApplicationLibraryFor("john");

        assertEquals(6, applications.size());

        assertApplication(applications, "a1", "Email");
        assertApplication(applications, "a2", "Agenda");
        assertApplication(applications, "a3", "Math4You");
        assertApplication(applications, "a4", "Biology Naturally");
        assertApplication(applications, "a5", "EduCloudwise Intranet");
        assertApplication(applications, "a6", "School Site");
    }


    /**
     * Verifies that organization-level applications override
     * root-level applications when no school override exists.
     */
    @Test
    void shouldReturnApplicationsForSunSchoolStudent() {

        List<Application> applications =
                applicationLibraryService.getApplicationLibraryFor("mary");

        assertEquals(6, applications.size());

        assertApplication(applications, "a1", "Gmail");
        assertApplication(applications, "a2", "Calendar");
        assertApplication(applications, "a3", "Math4You");
        assertApplication(applications, "a4", "Biology Naturally");
        assertApplication(applications, "a5", "EduCloudwise Intranet");
        assertApplication(applications, "a7", "School Site");
    }


    /**
     * Verifies that school-level applications override
     * organization-level applications.
     */
    @Test
    void shouldReturnApplicationsForRainbowStudent() {

        List<Application> applications =
                applicationLibraryService.getApplicationLibraryFor("peter");

        assertEquals(5, applications.size());

        assertApplication(applications, "a1", "Gmail");
        assertApplication(applications, "a2", "Calendar");
        assertApplication(applications, "a3", "Math4You");
        assertApplication(applications, "a4", "Biology Naturally");
        assertApplication(applications, "a5", "Intranet");
    }


    /**
     * Verifies that organization and root applications are returned
     * when no school applications exist.
     */
    @Test
    void shouldFallbackToOrganizationAndRootWhenSchoolHasNoApplications() {

        List<Application> applications =
                applicationLibraryService.getApplicationLibraryFor("bob");

        assertEquals(5, applications.size());

        assertApplication(applications, "a1", "Gmail");
        assertApplication(applications, "a2", "Organization Calendar");
        assertApplication(applications, "a3", "Math4You");
        assertApplication(applications, "a4", "Biology Naturally");
        assertApplication(applications, "a8", "Organization Portal");
    }


    /**
     * Verifies that only root applications are returned when neither
     * school nor organization applications exist.
     */
    @Test
    void shouldReturnOnlyRootApplicationsWhenSchoolAndOrganizationHaveNoApplications() {

        List<Application> applications =
                applicationLibraryService.getApplicationLibraryFor("alice");

        assertEquals(4, applications.size());

        assertApplication(applications, "a1", "Gmail");
        assertApplication(applications, "a2", "Agenda");
        assertApplication(applications, "a3", "Math4You");
        assertApplication(applications, "a4", "Biology Naturally");
    }
}
