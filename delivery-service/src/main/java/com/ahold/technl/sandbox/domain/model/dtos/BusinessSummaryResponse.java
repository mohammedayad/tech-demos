package com.ahold.technl.sandbox.domain.model.dtos;


/**
 * DTO representing a summary of business metrics.
 * Contains the total number of deliveries and the average time between delivery starts in minutes.
 */
public record BusinessSummaryResponse(
        long deliveries,
        long averageMinutesBetweenDeliveryStart
) {
}
