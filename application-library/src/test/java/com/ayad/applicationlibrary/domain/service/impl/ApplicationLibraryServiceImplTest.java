package com.ayad.applicationlibrary.domain.service.impl;


import com.ayad.applicationlibrary.common.exceptions.UserNotFoundException;
import com.ayad.applicationlibrary.domain.model.Application;
import com.ayad.applicationlibrary.domain.model.Organization;
import com.ayad.applicationlibrary.domain.model.School;
import com.ayad.applicationlibrary.domain.model.User;
import com.ayad.applicationlibrary.domain.repository.ifc.ApplicationRepository;
import com.ayad.applicationlibrary.domain.repository.ifc.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ayad.applicationlibrary.common.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ApplicationLibraryServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class ApplicationLibraryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationLibraryServiceImpl applicationLibraryService;

    private Organization organization;
    private School cloudCollege;
    private School sunSchool;
    private School rainbowSchool;

    @BeforeEach
    void setUp() {
        organization = new Organization();
        organization.setId(UUID.randomUUID());
        organization.setName("EduCloudwise");

        cloudCollege = new School();
        cloudCollege.setId(UUID.randomUUID());
        cloudCollege.setName("Cloud College");
        cloudCollege.setOrganization(organization);

        sunSchool = new School();
        sunSchool.setId(UUID.randomUUID());
        sunSchool.setName("Sun School");
        sunSchool.setOrganization(organization);

        rainbowSchool = new School();
        rainbowSchool.setId(UUID.randomUUID());
        rainbowSchool.setName("The Rainbow");
        rainbowSchool.setOrganization(organization);
    }

    /**
     * Verifies that school-level applications override
     * organization and root-level applications.
     */
    @Test
    void shouldResolveApplicationsForCloudCollegeStudent() {

        User user = createUser("john", cloudCollege);

        when(userRepository.findByUserId("john"))
                .thenReturn(Optional.of(user));

        when(applicationRepository.findAllApplicationsForOrganizationAndSchool(
                organization.getId(),
                cloudCollege.getId()))
                .thenReturn(List.of(
                        createRootApplication("a1", "Gmail"),
                        createRootApplication("a2", "Agenda"),
                        createRootApplication("a3", "Math4You"),
                        createRootApplication("a4", "Biology Naturally"),

                        createOrganizationApplication("a2", "Calendar"),
                        createOrganizationApplication("a5", "EduCloudwise Intranet"),

                        createSchoolApplication("a1", "Email"),
                        createSchoolApplication("a2", "Agenda"),
                        createSchoolApplication("a6", "School Site")
                ));

        List<Application> result =
                applicationLibraryService.getApplicationLibraryFor("john");

        assertEquals(6, result.size());

        assertApplication(result, "a1", "Email");
        assertApplication(result, "a2", "Agenda");
        assertApplication(result, "a3", "Math4You");
        assertApplication(result, "a4", "Biology Naturally");
        assertApplication(result, "a5", "EduCloudwise Intranet");
        assertApplication(result, "a6", "School Site");
    }


    /**
     * Verifies that organization-level applications override
     * root-level applications when no school override exists.
     */
    @Test
    void shouldResolveApplicationsForSunSchoolStudent() {

        User user = createUser("mary", sunSchool);

        when(userRepository.findByUserId("mary"))
                .thenReturn(Optional.of(user));

        when(applicationRepository.findAllApplicationsForOrganizationAndSchool(
                organization.getId(),
                sunSchool.getId()))
                .thenReturn(List.of(
                        createRootApplication("a1", "Gmail"),
                        createRootApplication("a2", "Agenda"),
                        createRootApplication("a3", "Math4You"),
                        createRootApplication("a4", "Biology Naturally"),

                        createOrganizationApplication("a2", "Calendar"),
                        createOrganizationApplication("a5", "EduCloudwise Intranet"),

                        createSchoolApplication("a7", "School Site")
                ));

        List<Application> result =
                applicationLibraryService.getApplicationLibraryFor("mary");

        assertEquals(6, result.size());

        assertApplication(result, "a1", "Gmail");
        assertApplication(result, "a2", "Calendar");
        assertApplication(result, "a3", "Math4You");
        assertApplication(result, "a4", "Biology Naturally");
        assertApplication(result, "a5", "EduCloudwise Intranet");
        assertApplication(result, "a7", "School Site");
    }


    @Test
    void shouldResolveApplicationsForRainbowSchoolStudent() {
        User user = createUser("Peter", rainbowSchool);
        when(userRepository.findByUserId("Peter")).thenReturn(Optional.of(user));

        when(applicationRepository.
                findAllApplicationsForOrganizationAndSchool(organization.getId(), rainbowSchool.getId())).
                thenReturn(List.of(createRootApplication("a1", "Gmail"),
                        createRootApplication("a2", "Agenda"),
                        createRootApplication("a3", "Math4You"),
                        createRootApplication("a4", "Biology Naturally"),

                        createOrganizationApplication("a2", "Calendar"),
                        createOrganizationApplication("a5", "EduCloudwise Intranet"),

                        createSchoolApplication("a5", "Intranet")

                ));

        List<Application> result = applicationLibraryService.getApplicationLibraryFor("Peter");
        assertEquals(5, result.size());

        assertApplication(result, "a1", "Gmail");
        assertApplication(result, "a2", "Calendar");
        assertApplication(result, "a3", "Math4You");
        assertApplication(result, "a4", "Biology Naturally");
        assertApplication(result, "a5", "Intranet");

    }


    @Test
    void shouldPreferSchoolOverOrganizationAndRoot() {

        User user = createUser("john", cloudCollege);

        when(userRepository.findByUserId("john"))
                .thenReturn(Optional.of(user));

        when(applicationRepository.findAllApplicationsForOrganizationAndSchool(
                organization.getId(),
                cloudCollege.getId()))
                .thenReturn(List.of(
                        createRootApplication("a1", "Gmail"),
                        createOrganizationApplication("a1", "Organization Outlook"),
                        createSchoolApplication("a1", "School Mail")
                ));

        List<Application> result =
                applicationLibraryService.getApplicationLibraryFor("john");

        assertEquals(1, result.size());

        assertApplication(result, "a1", "School Mail");
    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByUserId("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> applicationLibraryService.getApplicationLibraryFor("unknown")
        );
    }


    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationLibraryService.getApplicationLibraryFor(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsEmpty() {

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationLibraryService.getApplicationLibraryFor("")
        );
    }
}
