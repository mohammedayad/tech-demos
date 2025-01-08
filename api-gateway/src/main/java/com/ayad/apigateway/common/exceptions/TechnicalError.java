package com.ayad.apigateway.common.exceptions;

public class TechnicalError extends RuntimeException {
    public static final String TECHNICAL_ERROR_MESSAGE = "TECHNICAL_ERROR";

    public TechnicalError(Throwable cause) {
        super(TECHNICAL_ERROR_MESSAGE, cause);
    }
}
