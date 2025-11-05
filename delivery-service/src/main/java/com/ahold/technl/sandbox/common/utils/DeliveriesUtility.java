package com.ahold.technl.sandbox.common.utils;


import lombok.experimental.UtilityClass;


/**
 * Utility class for delivery-related constants and methods.
 */
@UtilityClass
public class DeliveriesUtility {
    public final String FINISHED_AT_MUST_BE_EMPTY = "finishedAt must be empty when status is IN_PROGRESS";
    public final String FINISHED_AT_MUST_BE_PROVIDED = "finishedAt must be provided when status is DELIVERED";

    public final String DELIVERY_IDS_CANNOT_BE_EMPTY = "Delivery IDs list cannot be null or empty";

    public static final String NO_DELIVERY_MATCHING_FOUND = "Delivery %s not found";

    public final String INVOICE_STATUS_SUCCESS = "Invoice generated successfully";

    public final String INVOICE_STATUS_NOT_FOUND = "Invoice not found for the given delivery";

    public static final String INVOICE_API_CALL_FAILED = "Failed to process invoice request for delivery %s";
    public final String ILLEGAL_ARGUMENT_ERROR_TITLE = "Illegal Argument Error";
    public final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";
    public final String SERVER_ERROR_MESSAGE = "Server Error";

    public final String SERVICE_PROCESSING_ERROR_TITLE = "Service Processing Error";
}
