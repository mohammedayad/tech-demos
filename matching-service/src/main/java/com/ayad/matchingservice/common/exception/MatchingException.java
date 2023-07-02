package com.ayad.matchingservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * custom Exception for handling the matching services exceptions.
 */
@Getter
public class MatchingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorMessage;

    public MatchingException(HttpStatus statusCode, String code, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = code;
        this.errorMessage = message;

    }
}
