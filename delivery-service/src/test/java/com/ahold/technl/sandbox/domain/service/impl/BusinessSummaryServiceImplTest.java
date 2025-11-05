package com.ahold.technl.sandbox.domain.service.impl;


import com.ahold.technl.sandbox.domain.model.dtos.BusinessSummaryResponse;
import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import com.ahold.technl.sandbox.domain.repository.ifc.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BusinessSummaryServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class BusinessSummaryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    private BusinessSummaryServiceImpl businessSummaryService;

    @BeforeEach
    void setUp() {
        // Initialize the service with the required ZoneId
        businessSummaryService = new BusinessSummaryServiceImpl(deliveryRepository, "Europe/Amsterdam");
    }


    /**
     * Tests the yesterdaySummary method when there are multiple deliveries.
     */
    @Test
    void testYesterdaySummary_WithMultipleDeliveries() {
        // Arrange
        Delivery delivery1 = new Delivery();
        delivery1.setStartedAt(Instant.parse("2025-09-13T08:00:00Z"));

        Delivery delivery2 = new Delivery();
        delivery2.setStartedAt(Instant.parse("2025-09-13T10:00:00Z"));

        Delivery delivery3 = new Delivery();
        delivery3.setStartedAt(Instant.parse("2025-09-13T12:00:00Z"));

        when(deliveryRepository.findAllByStartedAtBetweenOrderByStartedAtAsc(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(delivery1, delivery2, delivery3));

        // Act
        BusinessSummaryResponse response = businessSummaryService.yesterdaySummary();

        // Assert
        assertEquals(3, response.deliveries());
        assertEquals(120, response.averageMinutesBetweenDeliveryStart());
    }


    /**
     * Tests the yesterdaySummary method when there is only one delivery.
     */
    @Test
    void testYesterdaySummary_WithSingleDelivery() {
        // Arrange
        Delivery delivery1 = new Delivery();
        delivery1.setStartedAt(Instant.parse("2025-09-13T08:00:00Z"));

        when(deliveryRepository.findAllByStartedAtBetweenOrderByStartedAtAsc(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(delivery1));

        // Act
        BusinessSummaryResponse response = businessSummaryService.yesterdaySummary();

        // Assert
        assertEquals(1, response.deliveries());
        assertEquals(0, response.averageMinutesBetweenDeliveryStart());
    }


    /**
     * Tests the yesterdaySummary method when there are no deliveries.
     */
    @Test
    void testYesterdaySummary_WithNoDeliveries() {
        // Arrange
        when(deliveryRepository.findAllByStartedAtBetweenOrderByStartedAtAsc(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of());

        // Act
        BusinessSummaryResponse response = businessSummaryService.yesterdaySummary();

        // Assert
        assertEquals(0, response.deliveries());
        assertEquals(0, response.averageMinutesBetweenDeliveryStart());
    }


}
