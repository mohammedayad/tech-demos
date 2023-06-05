package com.ayad.microservicegateway.domain.service.impl;

import com.ayad.microservicegateway.clients.ifc.HystrixClient;
import com.ayad.microservicegateway.domain.service.ifc.ApiGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ApiGatewayServiceImpl implements ApiGatewayService {

    private final HystrixClient client;

    public ApiGatewayServiceImpl(HystrixClient client) {
        this.client = client;
    }

    @Override
    public String callDemoService() {
        log.info("current main thread {}",Thread.currentThread().getName());
        return client.handleRequest();
    }
}
