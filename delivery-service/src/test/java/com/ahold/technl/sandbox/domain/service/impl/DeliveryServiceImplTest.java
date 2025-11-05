package com.ahold.technl.sandbox.domain.service.impl;

import com.ahold.technl.sandbox.clients.proxy.ifc.InvoiceServiceClient;
import com.ahold.technl.sandbox.common.utils.DeliveriesUtility;
import com.ahold.technl.sandbox.domain.model.dtos.*;
import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import com.ahold.technl.sandbox.domain.model.entities.DeliveryStatus;
import com.ahold.technl.sandbox.domain.model.mapper.DeliveryMapper;
import com.ahold.technl.sandbox.domain.repository.ifc.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ahold.technl.sandbox.common.utils.DeliveriesUtility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link DeliveryServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private InvoiceServiceClient invoiceClient;
    @InjectMocks
    private DeliveryServiceImpl deliveryService;


    /**
     * Tests the successful creation of a delivery.
     */
    @Test
    void testCreateDelivery_Success() {
        // Arrange
        CreateDeliveryRequest request = CreateDeliveryRequest.builder()
                .vehicleId("vehicle123")
                .address("123 Main St")
                .startedAt(Instant.parse("2023-10-01T09:00:00Z"))
                .status(DeliveryStatus.DELIVERED)
                .finishedAt(Instant.parse("2023-10-01T10:00:00Z"))
                .build();

        Delivery delivery = new Delivery();
        UUID deliveryId = UUID.randomUUID();
        DeliveryResponse response =
                new DeliveryResponse(deliveryId,
                        request.getVehicleId(),
                        request.getAddress(),
                        request.getStartedAt(),
                        request.getFinishedAt(),
                        request.getStatus());

        when(deliveryMapper.toCreateDeliveryEntity(request)).thenReturn(delivery);
        when(deliveryRepository.save(delivery)).thenReturn(delivery);
        when(deliveryMapper.toDeliveryResponse(delivery)).thenReturn(response);

        // Act
        DeliveryResponse result = deliveryService.create(request);

        // Assert
        assertEquals(response, result);
        verify(deliveryRepository, times(1)).save(delivery);
    }


    /**
     * Tests that an exception is thrown when the status is IN_PROGRESS and finishedAt is not null.
     */
    @Test
    void testCreateDelivery_InvalidStatusInProgress() {
        // Arrange
        CreateDeliveryRequest request = CreateDeliveryRequest.builder()
                .vehicleId("vehicle123")
                .address("123 Main St")
                .startedAt(Instant.parse("2023-10-01T09:00:00Z"))
                .status(DeliveryStatus.IN_PROGRESS)
                .finishedAt(Instant.parse("2023-10-01T10:00:00Z"))
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> deliveryService.create(request));
        assertEquals(FINISHED_AT_MUST_BE_EMPTY, exception.getMessage());
    }


    /**
     * Tests that an exception is thrown when the status is DELIVERED and finishedAt is null.
     */
    @Test
    void testCreateDelivery_InvalidStatusDelivered() {
        // Arrange
        CreateDeliveryRequest request = CreateDeliveryRequest.builder()
                .vehicleId("vehicle123")
                .address("123 Main St")
                .startedAt(Instant.parse("2023-10-01T09:00:00Z"))
                .status(DeliveryStatus.DELIVERED)
                .finishedAt(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> deliveryService.create(request));
        assertEquals(DeliveriesUtility.FINISHED_AT_MUST_BE_PROVIDED, exception.getMessage());
    }

    /**
     * Tests the successful generation of invoices for multiple deliveries.
     */
    @Test
    void testGenerateInvoices_AllSuccess() {
        // Arrange
        UUID deliveryId1 = UUID.randomUUID();
        UUID deliveryId2 = UUID.randomUUID();
        Delivery delivery1 = Delivery.builder()
                .id(deliveryId1)
                .address("123 Main St")
                .status(DeliveryStatus.DELIVERED)
                .build();
        Delivery delivery2 = Delivery.builder()
                .id(deliveryId2)
                .address("456 Elm St")
                .status(DeliveryStatus.DELIVERED)
                .build();

        when(deliveryRepository.findById(deliveryId1)).thenReturn(Optional.of(delivery1));
        when(deliveryRepository.findById(deliveryId2)).thenReturn(Optional.of(delivery2));
        when(invoiceClient.sendInvoice(any(ThirdPartyInvoiceRequest.class)))
                .thenReturn(new ThirdPartyInvoiceResponse(UUID.randomUUID(), true))
                .thenReturn(new ThirdPartyInvoiceResponse(UUID.randomUUID(), true));

        // Act
        List<InvoiceResponse> responses = deliveryService.generateInvoices(List.of(deliveryId1, deliveryId2));

        // Assert
        assertEquals(2, responses.size());
        assertEquals(INVOICE_STATUS_SUCCESS, responses.get(0).message());
        assertEquals(INVOICE_STATUS_SUCCESS, responses.get(1).message());
        verify(deliveryRepository, times(2)).findById(any(UUID.class));
        verify(invoiceClient, times(2)).sendInvoice(any(ThirdPartyInvoiceRequest.class));
    }


    /**
     * Tests the generation of invoices when some deliveries are not found.
     */
    @Test
    void testGenerateInvoices_AllNotFound() {
        // Arrange
        UUID deliveryId1 = UUID.randomUUID();
        UUID deliveryId2 = UUID.randomUUID();

        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        List<InvoiceResponse> responses = deliveryService.generateInvoices(List.of(deliveryId1, deliveryId2));

        // Assert
        assertEquals(2, responses.size());
        assertEquals(INVOICE_STATUS_NOT_FOUND, responses.get(0).message());
        assertEquals(INVOICE_STATUS_NOT_FOUND, responses.get(1).message());
        verify(deliveryRepository, times(2)).findById(any(UUID.class));
        verifyNoInteractions(invoiceClient);
    }


    /**
     * Tests the generation of invoices when some deliveries are found and some are not.
     */
    @Test
    void testGenerateInvoices_MixedResults() {
        // Arrange
        UUID deliveryId1 = UUID.randomUUID();
        UUID deliveryId2 = UUID.randomUUID();
        Delivery delivery1 = Delivery.builder()
                .id(deliveryId1)
                .address("123 Main St")
                .status(DeliveryStatus.DELIVERED)
                .build();

        when(deliveryRepository.findById(deliveryId1)).thenReturn(Optional.of(delivery1));
        when(deliveryRepository.findById(deliveryId2)).thenReturn(Optional.empty());
        when(invoiceClient.sendInvoice(any(ThirdPartyInvoiceRequest.class)))
                .thenReturn(new ThirdPartyInvoiceResponse(UUID.randomUUID(), true));

        // Act
        List<InvoiceResponse> responses = deliveryService.generateInvoices(List.of(deliveryId1, deliveryId2));

        // Assert
        assertEquals(2, responses.size());
        assertEquals(INVOICE_STATUS_SUCCESS, responses.get(0).message());
        assertEquals(INVOICE_STATUS_NOT_FOUND, responses.get(1).message());
        verify(deliveryRepository, times(2)).findById(any(UUID.class));
        verify(invoiceClient, times(1)).sendInvoice(any(ThirdPartyInvoiceRequest.class));
    }


    /**
     * Tests that an exception is thrown when the delivery IDs list is null or empty.
     */
    @Test
    void testGenerateInvoices_EmptyDeliveryIds() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deliveryService.generateInvoices(List.of()));
        assertEquals(DELIVERY_IDS_CANNOT_BE_EMPTY, exception.getMessage());
        verifyNoInteractions(deliveryRepository, invoiceClient);
    }
}
