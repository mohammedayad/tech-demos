package com.ahold.technl.sandbox.domain.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a request to send an invoice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyInvoiceRequest {
    private UUID deliveryId;
    private String address;
}
