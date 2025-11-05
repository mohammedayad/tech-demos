package com.ahold.technl.sandbox.resources.rest.controller;


import com.ahold.technl.sandbox.domain.model.dtos.*;
import com.ahold.technl.sandbox.domain.service.ifc.BusinessSummaryService;
import com.ahold.technl.sandbox.domain.service.ifc.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller for managing deliveries.
 * Provides endpoints for creating and retrieving delivery information.
 */

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/deliveries")
@Tag(name = "Delivery APIs", description = "APIs for managing deliveries and generating invoices.")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final BusinessSummaryService summaryService;


    /**
     * Creates a new delivery.
     *
     * @param request the request containing delivery details
     * @return a {@link ResponseEntity} containing the created delivery details
     */
    @Operation(summary = "Creates a new delivery.", description = "Creates a new delivery based on the provided request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody CreateDeliveryRequest request) {
        log.info("Received request to create delivery: {}", request);
        DeliveryResponse response = deliveryService.create(request);
        log.info("Successfully created delivery with response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Generates invoices for the specified delivery IDs.
     * The HTTP status code is determined based on the results:
     *
     * @param request the request containing delivery IDs
     * @return a {@link ResponseEntity} containing a list of invoice responses
     */
    @Operation(summary = "Generate invoices", description = "Generates invoices for the specified delivery IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invoices generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "404", description = "One or more deliveries not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "207", description = "Partial success in generating invoices",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class)))
    })
    @PostMapping(path = "/invoice", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InvoiceResponse>> invoice(@Valid @RequestBody InvoiceRequest request) {
        log.info("Received request to generate invoices for delivery IDs: {}", request.getDeliveryIds());
        List<InvoiceResponse> invoiceResponses = deliveryService.generateInvoices(request.getDeliveryIds());
        boolean allSuccess = invoiceResponses.stream().allMatch(result -> InvoiceStatus.SUCCESS.equals(result.status()));
        boolean allFailed = invoiceResponses.stream().noneMatch(result -> InvoiceStatus.SUCCESS.equals(result.status()));
        HttpStatus status = allSuccess ? HttpStatus.CREATED : allFailed ? HttpStatus.NOT_FOUND : HttpStatus.MULTI_STATUS;
        log.info("Invoice processing completed with status: {}", status);
        return new ResponseEntity<>(invoiceResponses, status);

    }

    /**
     * Retrieves the business summary for yesterday.
     *
     * @return a {@link ResponseEntity} containing the business summary
     */
    @Operation(summary = "Get business summary", description = "Retrieves the business summary for yesterday's deliveries.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business summary retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessSummaryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class)))
    })
    @GetMapping("/business-summary")
    public ResponseEntity<BusinessSummaryResponse> businessSummary() {
        log.info("Received request for business summary");
        BusinessSummaryResponse summaryResponse = summaryService.yesterdaySummary();
        log.info("Successfully generated business summary: {}", summaryResponse);
        return new ResponseEntity<>(summaryResponse, HttpStatus.OK);
    }

    @GetMapping("/DB/business-summary")
    public ResponseEntity<Void> businessSummaryDB() {
        log.info("Received request for business summary");
        summaryService.yesterdaySummaryDB();
        log.info("Successfully generated business summary");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
