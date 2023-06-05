package com.ayad.microservicegateway.clients.impl;

import com.ayad.microservicegateway.clients.ifc.HystrixClient;
import com.ayad.microservicegateway.domain.service.ifc.HystrixRequestsService;
import feign.hystrix.HystrixFeign;
import org.springframework.stereotype.Component;

@Component
public class HystrixClientImpl implements HystrixClient {


    @Override
    public String handleRequest() {
        HystrixRequestsService service = HystrixFeign.builder()
                // Target REST resource
                .target(HystrixRequestsService.class,
                        // Server
                        "http://localhost:8080/",
                        // Fallback implemenation
                        () -> "no response (fallback) "+ "Hystrix thread name: " + Thread.currentThread().getName());
        return service.callDemoService();
    }
}
