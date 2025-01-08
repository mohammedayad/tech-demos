package com.ayad.apigateway.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.Ordered;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

@UtilityClass
public class FilterConstants {

    // Custom attributes for Spring Gateway Context
    static final String GW_ENCRYPTION_DETAILS = "GW_PQ_KEY_RESOLVER";
    static final String GW_DECRYPTION_ERROR = "GW_PQ_DECRYPTION_ERROR";
    static final String GW_DECRYPTION_SKIPPED = "GW_PQ_DECRYPTION_SKIPPED";
    static final String GW_HALT_PROCESSING = "GW_HALT_PROCESSING";

    // Public custom attributes for Spring Gateway Context
    public static final String GW_ADDED_HEADERS = FilterConstants.class.getPackageName()+ ".GW_ADDED_HEADERS";
    public static final String GW_IGNORED_HEADERS = "GW_IGNORED_HEADERS";

    // Filter order
    // Pre-filters
    static final int CLIENT_ID_FILTER_ORDER_BEFORE_DECRYPTION = 949;
    static final int REQUEST_DECRYPTION_FILTER_ORDER = 950;
    public final int CLIENT_ID_FILTER_ORDER_AFTER_DECRYPTION = 951;
    public final int CHECK_REQUEST_FILTER_ORDER_AFTER_DECRYPTION = 953;
    /**
     * This filter is created by rate limiter library (bucket4j). Check application.yml for details.
     */
    @SuppressWarnings("unused")
    static final int RATE_LIMITER_FILTER_ORDER = 952;
    static final int IDENTIFY_ROUTING_FILTER_ORDER = 953;
    static final int SIGNING_KEY_FILTER_ORDER = 980;
    static final int REMOVE_HEADERS_FILTER_ORDER = 990;

    // Post-filters
    static final int RESPONSE_AUTHENTICATION_ERROR_FILTER_ORDER = WRITE_RESPONSE_FILTER_ORDER - 10;
    static final int IDENTIFY_RESPONSE_FILTER_ORDER = WRITE_RESPONSE_FILTER_ORDER - 11;
    // after response encryption it is not possible to put changes to response body anymore
    public static final int RESPONSE_ENCRYPTION_FILTER_ORDER = WRITE_RESPONSE_FILTER_ORDER - 12;
}
