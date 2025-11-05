package com.ahold.technl.sandbox.resources.rest.controller;


import com.ahold.technl.sandbox.domain.model.dtos.*;
import com.ahold.technl.sandbox.domain.model.entities.DeliveryStatus;
import com.ahold.technl.sandbox.domain.service.ifc.BusinessSummaryService;
import com.ahold.technl.sandbox.domain.service.ifc.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.ahold.technl.sandbox.common.utils.DeliveriesUtility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for {@link DeliveryController}.
 * Verifies the behavior of the REST endpoints for managing deliveries.
 */
@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeliveryService deliveryService;

    @MockitoBean
    private BusinessSummaryService summaryService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Tests the successful creation of a delivery.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testCreateDelivery_Success() throws Exception {
        // Arrange
        Instant finishedAt = Instant.parse("2023-10-01T10:00:00Z");
        CreateDeliveryRequest request = createDeliveryRequest(DeliveryStatus.DELIVERED, finishedAt);

        UUID deliveryId = UUID.randomUUID();
        DeliveryResponse response =
                new DeliveryResponse(deliveryId,
                        request.getVehicleId(),
                        request.getAddress(),
                        request.getStartedAt(),
                        finishedAt,
                        request.getStatus());

        Mockito.when(deliveryService.create(any(CreateDeliveryRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryId.toString()));
    }


    /**
     * Tests that a bad request response is returned for an invalid request.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testCreateDelivery_InvalidRequest() throws Exception {
        // Arrange
        CreateDeliveryRequest request = new CreateDeliveryRequest(); // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * Tests that an exception is thrown when the status is DELIVERED but finishedAt is null.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testCreateDelivery_DeliveredWithoutFinishedAt() throws Exception {
        // Arrange
        CreateDeliveryRequest request = createDeliveryRequest(DeliveryStatus.DELIVERED, null);

        Mockito.when(deliveryService.create(any(CreateDeliveryRequest.class))).thenThrow(
                new IllegalArgumentException(FINISHED_AT_MUST_BE_PROVIDED));

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(FINISHED_AT_MUST_BE_PROVIDED,
                        result.getResolvedException().getMessage()))
                .andExpect(jsonPath("$.detail").value(FINISHED_AT_MUST_BE_PROVIDED));

    }


    /**
     * Tests that an exception is thrown when the status is IN_PROGRESS but finishedAt is not null.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testCreateDelivery_InProgressWithFinishedAt() throws Exception {
        // Arrange
        Instant finishedAt = Instant.parse("2023-10-01T10:00:00Z");
        CreateDeliveryRequest request = createDeliveryRequest(DeliveryStatus.IN_PROGRESS, finishedAt);

        Mockito.when(deliveryService.create(any(CreateDeliveryRequest.class))).thenThrow(
                new IllegalArgumentException(FINISHED_AT_MUST_BE_EMPTY));

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(FINISHED_AT_MUST_BE_EMPTY,
                        result.getResolvedException().getMessage()))
                .andExpect(jsonPath("$.detail").value(FINISHED_AT_MUST_BE_EMPTY));

    }


    /**
     * Tests the invoice endpoint when all invoice generations are successful.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testInvoice_AllSuccess() throws Exception {
        // Arrange
        UUID deliveryID1 = UUID.randomUUID();
        UUID deliveryID2 = UUID.randomUUID();
        InvoiceRequest request = new InvoiceRequest(List.of(deliveryID1, deliveryID2));
        List<InvoiceResponse> responses = List.of(
                new InvoiceResponse(deliveryID1, UUID.randomUUID(),
                        InvoiceStatus.SUCCESS,
                        INVOICE_STATUS_SUCCESS),
                new InvoiceResponse(deliveryID2, UUID.randomUUID(),
                        InvoiceStatus.SUCCESS,
                        INVOICE_STATUS_SUCCESS)
        );

        Mockito.when(deliveryService.generateInvoices(anyList())).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$[1].status").value("SUCCESS"));
    }


    /**
     * Tests the invoice endpoint when all invoice generations fail.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testInvoice_AllFailed() throws Exception {
        // Arrange
        UUID deliveryID1 = UUID.randomUUID();
        UUID deliveryID2 = UUID.randomUUID();
        InvoiceRequest request = new InvoiceRequest(List.of(deliveryID1, deliveryID2));
        List<InvoiceResponse> responses = List.of(
                new InvoiceResponse(deliveryID1, null, InvoiceStatus.NOT_FOUND, INVOICE_STATUS_NOT_FOUND),
                new InvoiceResponse(deliveryID2, null, InvoiceStatus.NOT_FOUND, INVOICE_STATUS_NOT_FOUND)
        );

        Mockito.when(deliveryService.generateInvoices(anyList())).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("NOT_FOUND"))
                .andExpect(jsonPath("$[1].status").value("NOT_FOUND"));
    }


    /**
     * Tests the invoice endpoint with a mix of successful and failed invoice generations.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testInvoice_PartialSuccess() throws Exception {
        // Arrange
        UUID deliveryID1 = UUID.randomUUID();
        UUID deliveryID2 = UUID.randomUUID();
        InvoiceRequest request = new InvoiceRequest(List.of(deliveryID1, deliveryID2));
        List<InvoiceResponse> responses = List.of(
                new InvoiceResponse(deliveryID1, UUID.randomUUID(), InvoiceStatus.SUCCESS, INVOICE_STATUS_SUCCESS),
                new InvoiceResponse(deliveryID2, null, InvoiceStatus.NOT_FOUND, INVOICE_STATUS_NOT_FOUND)
        );

        Mockito.when(deliveryService.generateInvoices(anyList())).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/deliveries/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isMultiStatus())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$[1].status").value("NOT_FOUND"));
    }


    /**
     * Tests the successful retrieval of the business summary.
     *
     * @throws Exception if the request or response processing fails
     */
    @Test
    void testBusinessSummary_Success() throws Exception {
        // Arrange
        BusinessSummaryResponse mockResponse = new BusinessSummaryResponse(10, 15);
        when(summaryService.yesterdaySummary()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/deliveries/business-summary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deliveries").value(10))
                .andExpect(jsonPath("$.averageMinutesBetweenDeliveryStart").value(15));
    }


    /**
     * Helper method to create a {@link CreateDeliveryRequest} with the specified status and finishedAt timestamp.
     *
     * @param deliveryStatus the status of the delivery
     * @param finishedAt     the finishedAt timestamp
     * @return a {@link CreateDeliveryRequest} instance
     */
    private CreateDeliveryRequest createDeliveryRequest(DeliveryStatus deliveryStatus, Instant finishedAt) {
        return CreateDeliveryRequest.builder()
                .vehicleId("vehicle123")
                .address("123 Main St")
                .startedAt(Instant.parse("2023-10-01T09:00:00Z"))
                .status(deliveryStatus)
                .finishedAt(finishedAt)
                .build();
    }
}
