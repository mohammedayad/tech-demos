package com.ayad.apigateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

/**
 * This is helper class for rate limiting configuration
 **/
@Slf4j
@Service("RateLimiterHelperService")
public class RateLimiterHelperService {
    private static final String KEY = "key";
    private static final String HARDWARE_ID_HEADER = "localhost:8080";

    /**
     * Provides hardwareId header which will be used for rate limiting
     * Note that using a getHeader('...') directly in the expression property doesn't work, because that
     * only reports the original (encrypted) headers of the request.
     *
     * @return hardwareId
     */
    public String getHardwareId(ServerHttpRequest request) {
        return request.getHeaders()
                .getFirst(HARDWARE_ID_HEADER.toLowerCase());
    }

    /**
     * Returns form parameter "key" from the POST /v2/customers/emailOptIn requests
     *
     * @return encryptedId
     */
    public String getKeyForEmailOptIn(ServerHttpRequest request) {
        return request
                .getQueryParams()
                .getFirst(KEY);
    }

    /**
     * Returns if the current request is POST
     *
     * @return boolean
     */
    public boolean executeForPost(ServerHttpRequest request) {
        return HttpMethod.GET.equals(request.getMethod());

    }


    public boolean test() {
        return true;

    }

    public boolean logRoot(Object root) {
        log.info("Root object type: {}", root.getClass());
        return true;
    }

}
