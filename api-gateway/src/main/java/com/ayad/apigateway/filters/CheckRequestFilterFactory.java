package com.ayad.apigateway.filters;

import com.ayad.apigateway.common.exceptions.Exceptions;
import io.micrometer.observation.Observation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.ayad.apigateway.common.utils.FilterConstants.*;

// second pre filter
@Slf4j
@Component
public class CheckRequestFilterFactory extends AbstractGatewayConditionalPreFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("CustomPostProcessingFilter: Starting filter for request: {}", exchange.getRequest().getPath());
        Observation gatewayObservation = exchange
                .getAttribute(ServerWebExchangeUtils.GATEWAY_OBSERVATION_ATTR);
        // org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gateway.observation
        ServerRequestObservationContext serverRequestObservationContext = exchange
                .getAttribute("org.springframework.http.server.reactive.observation.ServerRequestObservationContext");
        addRequestHeaders(exchange);

        log.info("TraceId: [{}], SpanId: [{}]", MDC.get("traceId"), MDC.get("spanId"));
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> log.info("CustomPostProcessingFilter: Successfully passed through the chain."))
                .doOnTerminate(() -> {
                    boolean isCommitted = exchange.getResponse().isCommitted();
                    log.info("CustomPostProcessingFilter: Is response committed? {}", isCommitted);

                    if (!isCommitted) {
                        log.info("CustomPostProcessingFilter: Modifying response headers as response is not committed.");
                        exchange.getResponse().getHeaders().add("X-Custom-Header", "ProcessedAfterException");
                    } else {
                        log.warn("CustomPostProcessingFilter: Response is already committed. No modifications possible.");
                    }
                })
                .doOnError(err -> log.error("CustomPostProcessingFilter: Error occurred - {}", err.getMessage()))
                .doFinally(signalType -> log.info("CustomPostProcessingFilter: Filter finalized with signal: {}", signalType));
    }

    private void addRequestHeaders(ServerWebExchange exchange){
        Map<String, String> headersToAdd =  new HashMap<>();
        headersToAdd.put("X-Custom-Header", "ProcessedAfterException");
        exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    headersToAdd.forEach(httpHeaders::set);
                })
                .build();
    }

    @Override
    public int getOrder() {
        return CHECK_REQUEST_FILTER_ORDER_AFTER_DECRYPTION;
    }
}
