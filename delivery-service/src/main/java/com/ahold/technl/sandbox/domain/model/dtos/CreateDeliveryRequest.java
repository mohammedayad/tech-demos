package com.ahold.technl.sandbox.domain.model.dtos;

import com.ahold.technl.sandbox.domain.model.entities.DeliveryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeliveryRequest {
    @NotBlank
    private String vehicleId;
    @NotBlank
    private String address;
    @NotNull
    private Instant startedAt;
    @NotNull
    private DeliveryStatus status;
    private Instant finishedAt;
}
