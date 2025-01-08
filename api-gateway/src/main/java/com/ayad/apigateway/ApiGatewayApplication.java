package com.ayad.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Hooks;


@EnableCaching
@SpringBootApplication
public class ApiGatewayApplication {


    public static void main(String[] args) {
        BlockHound.install(); // Detect blocking calls in reactive pipelines
        Hooks.enableAutomaticContextPropagation(); // propagate tracing context [traceId and spanId] this will be helpful to retrieve tracing properties from MDC, buy default these properties added by micrometer to MDC
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
