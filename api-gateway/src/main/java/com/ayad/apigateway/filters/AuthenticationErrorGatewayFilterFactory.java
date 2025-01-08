package com.ayad.apigateway.filters;

import com.ayad.apigateway.common.exceptions.Exceptions;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationView;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ayad.apigateway.common.utils.FilterConstants.*;

// first pre filter
@Slf4j
@Component
public class AuthenticationErrorGatewayFilterFactory extends AbstractGatewayConditionalPreFilter {

    private final Tracer tracer;

    public AuthenticationErrorGatewayFilterFactory(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Observation gatewayObservation = exchange
                .getAttribute(ServerWebExchangeUtils.GATEWAY_OBSERVATION_ATTR);

//        if(gatewayObservation!=null) {
//            gatewayObservation.scoped(() -> {
//                String traceId = tracer.currentSpan().context().traceId();
//                log.info("<ACCEPTANCE_TEST> <TRACE:{}> Hello from consumer", traceId);
//            });
//        }
        // org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gateway.observation
        ServerRequestObservationContext serverRequestObservationContext = exchange
                    .getAttribute("org.springframework.http.server.reactive.observation.ServerRequestObservationContext");
        // Get all query parameters
        MultiValueMap<String, String> queryParams = exchange.getRequest()
                .getQueryParams();
        // Example: Get a specific query parameter value
        boolean throwException = Boolean.parseBoolean(queryParams.getFirst("exception")); // Retrieves the first value of "id"
        log.info("TraceId: [{}], SpanId: [{}]", MDC.get("traceId"), MDC.get("spanId"));

        log.info("throwException {}", throwException);
        if (throwException) {
            throw Exceptions.unsupportedClient();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return CLIENT_ID_FILTER_ORDER_AFTER_DECRYPTION;
    }
}
