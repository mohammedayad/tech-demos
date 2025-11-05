package com.ahold.technl.sandbox.domain.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


/**
 * DTO representing a request to generate an invoice.
 * Contains a list of delivery IDs for which the invoice is to be created.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
    @NotEmpty
    private List<UUID> deliveryIds;
}
