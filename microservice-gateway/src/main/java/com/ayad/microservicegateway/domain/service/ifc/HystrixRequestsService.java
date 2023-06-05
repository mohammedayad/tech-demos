package com.ayad.microservicegateway.domain.service.ifc;

import feign.RequestLine;

public interface HystrixRequestsService {

    @RequestLine("GET /api/test/v1")
    public String callDemoService();
}
