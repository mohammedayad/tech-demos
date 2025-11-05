package com.ahold.technl.sandbox.common.exceptions;


/**
 * custom Exception for handling the general DeliveryProcessing exceptions.
 */
public class DeliveryProcessingException extends RuntimeException {
    public DeliveryProcessingException(String message) {
        super(message);
    }
}
