package com.ayad.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Abstract filter to execute PRE filter
 * PRE filters applied to the request received from the upstream bafore forward it to the downstream service
 */
public abstract class AbstractGatewayConditionalPreFilter
        extends AbstractGatewayFilterFactory<AbstractGatewayConditionalPreFilter.Config> {

    protected AbstractGatewayConditionalPreFilter() {
        super(AbstractGatewayConditionalPreFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(AbstractGatewayConditionalPreFilter.Config config) {
        return new NamedOrderedGatewayFilter(
                this.getClass().getSimpleName(),
                getOrder(),
                (exchange, chain) -> {
                    if (shouldFilter(exchange)) {
                        // execute filter
                        return filter(exchange, chain);
                    }
                    // go to next filter in the chain
                    return chain.filter(exchange);
                }
        );
    }

    public abstract Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);

    public boolean shouldFilter(ServerWebExchange exchange) {
        return true;
    }
    public abstract int getOrder();

    public static class Config {
    }
}
