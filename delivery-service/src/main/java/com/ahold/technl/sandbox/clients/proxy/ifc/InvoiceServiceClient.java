package com.ahold.technl.sandbox.clients.proxy.ifc;


import com.ahold.technl.sandbox.domain.model.dtos.ThirdPartyInvoiceRequest;
import com.ahold.technl.sandbox.domain.model.dtos.ThirdPartyInvoiceResponse;

public interface InvoiceServiceClient {

    ThirdPartyInvoiceResponse sendInvoice(ThirdPartyInvoiceRequest request);
}
