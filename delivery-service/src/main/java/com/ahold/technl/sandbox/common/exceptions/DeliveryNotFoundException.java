package com.ahold.technl.sandbox.common.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;


/**
 * custom Exception for handling the DeliveryNotFound exceptions.
 */
public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(String message) {
        super(message);
    }
}
