package com.ahold.technl.sandbox.domain.model.dtos;

import java.util.UUID;


/**
 * DTO representing a response for an invoice.
 * Contains the delivery ID and the associated invoice ID.
 */
public record InvoiceResponse(
        UUID deliveryId,
        UUID invoiceId,
        InvoiceStatus status, // SUCCESS, NOT_FOUND, FAILED
        String message // Additional information about the invoice generation process
) {
}
