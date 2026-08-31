package com.ayad.applicationlibrary.domain.service.impl;

import com.ayad.applicationlibrary.common.exceptions.UserNotFoundException;
import com.ayad.applicationlibrary.domain.model.*;
import com.ayad.applicationlibrary.domain.repository.ifc.ApplicationRepository;
import com.ayad.applicationlibrary.domain.repository.ifc.UserRepository;
import com.ayad.applicationlibrary.domain.service.ifc.ApplicationLibraryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Service for retrieving applications available to a user.
 *
 * Applications are resolved according to the hierarchy:
 * School > Organization > Root.
 */
@Service
public class ApplicationLibraryServiceImpl implements ApplicationLibraryService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationLibraryServiceImpl(UserRepository userRepository,
                                         ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }


    /**
     * Returns all applications available to the given user.
     *
     * Applications defined at a more specific level override
     * applications with the same identifier defined at a broader level.
     *
     * @param userId user identifier
     * @return resolved list of applications
     * @throws IllegalArgumentException if the user identifier is null or blank
     * @throws UserNotFoundException if the user does not exist
     */

    @Transactional(readOnly = true)
    @Override
    public List<Application> getApplicationLibraryFor(String userId) {

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId can not be empty");
        }

        // creat custom exception
        User user = userRepository.findByUserId(userId).
                orElseThrow(() -> new UserNotFoundException(userId));
        School school = user.getSchool();
        Organization organization = school.getOrganization();
        List<Application> userApplications = applicationRepository.
                findAllApplicationsForOrganizationAndSchool(organization.getId(), school.getId());

        Map<String, Application> resolvedApplications = new LinkedHashMap<>();

        userApplications.stream()
                .filter(a -> a.getLevel() == ApplicationLevel.ROOT)
                .forEach(a -> resolvedApplications.put(a.getApplicationId(), a));

        userApplications.stream()
                .filter(a -> a.getLevel() == ApplicationLevel.ORGANIZATION)
                .forEach(a -> resolvedApplications.put(a.getApplicationId(), a));

        userApplications.stream()
                .filter(a -> a.getLevel() == ApplicationLevel.SCHOOL)
                .forEach(a -> resolvedApplications.put(a.getApplicationId(), a));

        return new ArrayList<>(resolvedApplications.values());
    }

}
