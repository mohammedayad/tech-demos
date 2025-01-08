package com.ayad.integrationservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * custom Exception for handling the ProcessIdNotFound exceptions.
 */
@Getter
public class ProcessIdNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorMessage;

    public ProcessIdNotFoundException(HttpStatus statusCode, String code, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = code;
        this.errorMessage = message;

    }
}
