package com.ahold.technl.sandbox.domain.service.ifc;

import com.ahold.technl.sandbox.domain.model.dtos.CreateDeliveryRequest;
import com.ahold.technl.sandbox.domain.model.dtos.DeliveryResponse;
import com.ahold.technl.sandbox.domain.model.dtos.InvoiceResponse;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {

    DeliveryResponse create(CreateDeliveryRequest createDeliveryRequest);

    List<InvoiceResponse> generateInvoices(List<UUID> deliveryIds);
}
