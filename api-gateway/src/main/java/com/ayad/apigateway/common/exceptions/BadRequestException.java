package com.ayad.apigateway.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends TaggedException {
    public static final String BAD_REQUEST_ERROR_MESSAGE = "BAD REQUEST";

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, BAD_REQUEST_ERROR_MESSAGE, message);
    }
}
