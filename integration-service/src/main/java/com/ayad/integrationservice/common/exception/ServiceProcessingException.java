package com.ayad.integrationservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorMessage;

    public ServiceProcessingException(HttpStatus statusCode, String code, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = code;
        this.errorMessage = message;

    }
}
