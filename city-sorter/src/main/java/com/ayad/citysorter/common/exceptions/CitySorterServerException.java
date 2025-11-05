package com.ayad.citysorter.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;


/**
 * Custom exception class for handling errors in the City Sorter service.
 * This exception extends {@link RuntimeException} and includes additional details
 * such as HTTP status code, error code, and a descriptive error message.
 */
@Getter
public class CitySorterServerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorMessage;

    public CitySorterServerException(HttpStatus statusCode, String code, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = code;
        this.errorMessage = message;

    }
}
