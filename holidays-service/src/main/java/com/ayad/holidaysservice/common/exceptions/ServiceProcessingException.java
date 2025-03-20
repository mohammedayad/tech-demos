package com.ayad.holidaysservice.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class ServiceProcessingException extends RuntimeException {

    @Serial
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
