package com.ahold.technl.sandbox.domain.model.dtos;
import com.ahold.technl.sandbox.domain.model.entities.DeliveryStatus;

import java.time.Instant;
import java.util.UUID;


public record DeliveryResponse(
        UUID id,
        String vehicleId,
        String address,
        Instant startedAt,
        Instant finishedAt,
        DeliveryStatus status
) {
}
