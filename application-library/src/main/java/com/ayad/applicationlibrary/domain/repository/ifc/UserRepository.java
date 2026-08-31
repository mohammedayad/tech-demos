package com.ayad.applicationlibrary.domain.repository.ifc;

import com.ayad.applicationlibrary.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


/**
 * Repository for managing user entities.
 */
public interface UserRepository extends JpaRepository<User, UUID> {


    /**
     * Returns the user associated with the given user identifier.
     *
     * @param userId user identifier
     * @return matching user if found
     */
    Optional<User> findByUserId(String userId);
}
