package com.ayad.applicationlibrary.common.utils;

import com.ayad.applicationlibrary.domain.model.Application;
import com.ayad.applicationlibrary.domain.model.ApplicationLevel;
import com.ayad.applicationlibrary.domain.model.School;
import com.ayad.applicationlibrary.domain.model.User;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Utility methods for creating test data and assertions.
 */
@UtilityClass
public class TestUtils {


    /**
     * Creates a test user belonging to the given school.
     *
     * @param userId user identifier
     * @param school user's school
     * @return test user
     */
    public static User createUser(String userId, School school) {

        User user = new User();
        user.setUserId(userId);
        user.setSchool(school);
        return user;
    }


    /**
     * Creates a root-level application.
     *
     * @param applicationId application identifier
     * @param name application name
     * @return root application
     */
    public static Application createRootApplication(String applicationId, String name) {

        Application app = new Application();
        app.setApplicationId(applicationId);
        app.setName(name);
        app.setLevel(ApplicationLevel.ROOT);
        return app;
    }


    /**
     * Creates an organization-level application.
     *
     * @param applicationId application identifier
     * @param name application name
     * @return organization application
     */
    public static Application createOrganizationApplication(String applicationId, String name) {

        Application app = new Application();
        app.setApplicationId(applicationId);
        app.setName(name);
        app.setLevel(ApplicationLevel.ORGANIZATION);

        return app;
    }


    /**
     * Creates a school-level application.
     *
     * @param applicationId application identifier
     * @param name application name
     * @return school application
     */
    public static Application createSchoolApplication(String applicationId, String name) {

        Application app = new Application();
        app.setApplicationId(applicationId);
        app.setName(name);
        app.setLevel(ApplicationLevel.SCHOOL);

        return app;
    }



    /**
     * Verifies that the expected application exists in the result set.
     *
     * @param applications resolved applications
     * @param applicationId application identifier
     * @param expectedName expected application name
     */
    public static void assertApplication(
            List<Application> applications,
            String applicationId,
            String expectedName) {

        Application application = applications.stream()
                .filter(a -> applicationId.equals(a.getApplicationId()))
                .findFirst()
                .orElseThrow();

        assertEquals(expectedName, application.getName());
    }
}
