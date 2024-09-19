package com.example.testservice.exceptions;


/**
 * Custom exception class representing a retryable exception.
 * This exception extends RuntimeException and implements the Retryable interface.
 * It can be thrown to indicate that an operation is retryable.
 */
public class RetryableException extends RuntimeException implements Retryable {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
