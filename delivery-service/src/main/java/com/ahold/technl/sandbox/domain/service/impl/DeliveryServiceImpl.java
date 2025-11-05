package com.ahold.technl.sandbox.domain.service.impl;

import com.ahold.technl.sandbox.clients.proxy.ifc.InvoiceServiceClient;
import com.ahold.technl.sandbox.common.exceptions.DeliveryNotFoundException;
import com.ahold.technl.sandbox.domain.model.dtos.*;
import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import com.ahold.technl.sandbox.domain.model.entities.DeliveryStatus;
import com.ahold.technl.sandbox.domain.model.mapper.DeliveryMapper;
import com.ahold.technl.sandbox.domain.repository.ifc.DeliveryRepository;
import com.ahold.technl.sandbox.domain.service.ifc.DeliveryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ahold.technl.sandbox.common.utils.DeliveriesUtility.*;


/**
 * Implementation of the {@link DeliveryService} interface.
 * Handles the business logic for creating and managing deliveries.
 */
@Slf4j
@AllArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final InvoiceServiceClient invoiceClient;


    /**
     * Creates a new delivery based on the provided request.
     *
     * @param createDeliveryRequest the request containing delivery details
     * @return the response containing the created delivery details
     * @throws IllegalArgumentException if the request violates business rules
     */
    @Override
    @Transactional
    public DeliveryResponse create(CreateDeliveryRequest createDeliveryRequest) {
        if (createDeliveryRequest.getStatus() == DeliveryStatus.IN_PROGRESS && createDeliveryRequest.getFinishedAt() != null) {
            log.error("Validation failed: {}", FINISHED_AT_MUST_BE_EMPTY);
            throw new IllegalArgumentException(FINISHED_AT_MUST_BE_EMPTY);
        }
        if (createDeliveryRequest.getStatus() == DeliveryStatus.DELIVERED && createDeliveryRequest.getFinishedAt() == null) {
            log.error("Validation failed: {}", FINISHED_AT_MUST_BE_PROVIDED);
            throw new IllegalArgumentException(FINISHED_AT_MUST_BE_PROVIDED);
        }
        log.debug("Mapping CreateDeliveryRequest to Delivery entity");
        Delivery newDelivery = deliveryMapper.toCreateDeliveryEntity(createDeliveryRequest);
        log.debug("Saving new delivery to the database");
        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        log.info("Successfully created delivery with ID: {}", savedDelivery.getId());
        return deliveryMapper.toDeliveryResponse(savedDelivery);
    }


    /**
     * Generates invoices for the specified delivery IDs.
     *
     * @param deliveryIds the list of delivery IDs for which to generate invoices
     * @return a list of {@link InvoiceResponse} containing the results of the invoice generation
     * @throws IllegalArgumentException if the deliveryIds list is null or empty
     */
    @Override
    public List<InvoiceResponse> generateInvoices(List<UUID> deliveryIds) {
        log.info("Starting invoice generation for delivery IDs: {}", deliveryIds);
        if (deliveryIds == null || deliveryIds.isEmpty()) {
            log.error("Validation failed: {}", DELIVERY_IDS_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(DELIVERY_IDS_CANNOT_BE_EMPTY);
        }
        List<InvoiceResponse> invoiceResponses = deliveryIds.stream()
                .map(id -> {
                    try {
                        log.debug("Processing delivery ID: {}", id);
                        var delivery = deliveryRepository.findById(id)
                                .orElseThrow(() -> new DeliveryNotFoundException(NO_DELIVERY_MATCHING_FOUND.formatted(id)));
                        log.info("Found delivery for ID: {}", id);
                        log.info("Generating invoice for delivery ID: {}", id);
                        ThirdPartyInvoiceRequest invoiceRequest = new ThirdPartyInvoiceRequest(delivery.getId(), delivery.getAddress());
                        var invoice = invoiceClient.sendInvoice(invoiceRequest);
                        log.info("Successfully generated invoice for delivery ID: {}", id);
                        return new InvoiceResponse(delivery.getId(), invoice.getId(), InvoiceStatus.SUCCESS, INVOICE_STATUS_SUCCESS);
                    } catch (DeliveryNotFoundException e) {
                        log.warn("Delivery ID {} not found: {}", id, e.getMessage());
                        return new InvoiceResponse(id, null, InvoiceStatus.NOT_FOUND, INVOICE_STATUS_NOT_FOUND);

                    }
                })
//                .filter(Objects::nonNull) // Exclude null responses
                .toList();
        log.info("Completed invoice generation. Total invoices generated: {}", invoiceResponses.size());
        return invoiceResponses;

    }
}
