package com.ahold.technl.sandbox.domain.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a response from the invoice service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyInvoiceResponse {
    private UUID id;
    private boolean sent;
}
