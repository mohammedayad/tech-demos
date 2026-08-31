package com.ayad.applicationlibrary.common.exceptions;


/**
 * Thrown when a user cannot be found for a given identifier.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super(String.format("User not found for userId '%s'", userId));
    }
}
