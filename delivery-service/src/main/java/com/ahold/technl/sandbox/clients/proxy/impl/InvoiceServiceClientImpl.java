package com.ahold.technl.sandbox.clients.proxy.impl;

import com.ahold.technl.sandbox.clients.proxy.ifc.InvoiceServiceClient;
import com.ahold.technl.sandbox.common.exceptions.DeliveryProcessingException;
import com.ahold.technl.sandbox.domain.model.dtos.ThirdPartyInvoiceRequest;
import com.ahold.technl.sandbox.domain.model.dtos.ThirdPartyInvoiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.ahold.technl.sandbox.common.utils.DeliveriesUtility.*;

/**
 * Implementation of the {@link InvoiceServiceClient} interface.
 * Handles communication with the external invoice service.
 */
@Slf4j
@Component
public class InvoiceServiceClientImpl implements InvoiceServiceClient {

    private final RestTemplate restTemplate;
    private final String invoiceServiceUrl;

    public InvoiceServiceClientImpl(RestTemplate restTemplate, @Value("${app.invoice-service.url}") String invoiceServiceUrl) {
        this.restTemplate = restTemplate;
        this.invoiceServiceUrl = invoiceServiceUrl;
    }


    /**
     * Sends an invoice request to the invoice service.
     *
     * @param request the invoice request
     * @return the invoice response
     */
    @Override
    public ThirdPartyInvoiceResponse sendInvoice(ThirdPartyInvoiceRequest request) {
        try {
            log.info("Sending invoice request to invoice service: {}", request);

            String url = String.format("%s/v1/invoices", invoiceServiceUrl);
            ThirdPartyInvoiceResponse response = restTemplate.postForObject(url, request, ThirdPartyInvoiceResponse.class);

            log.info("Received invoice response from invoice service: {}", response);
            return response;

        } catch (Exception exception) {
            log.error("Error while calling invoice API for delivery ID {}: {}", request.getDeliveryId(), exception.getMessage(), exception);
            throw new DeliveryProcessingException(INVOICE_API_CALL_FAILED.formatted(request.getDeliveryId()));
        }
    }
}
