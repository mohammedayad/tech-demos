package com.ayad.stockservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * custom Exception for handling the StockNotFound exceptions.
 */
@Getter
public class StockNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorMessage;

    public StockNotFoundException(HttpStatus statusCode, String code, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = code;
        this.errorMessage = message;

    }
}
