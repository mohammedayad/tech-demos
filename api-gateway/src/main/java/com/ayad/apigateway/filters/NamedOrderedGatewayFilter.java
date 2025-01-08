package com.ayad.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;

public class NamedOrderedGatewayFilter extends OrderedGatewayFilter {
    private final String name;

    public NamedOrderedGatewayFilter(String name, int order, GatewayFilter delegate) {
        super(delegate, order);
        this.name = name;
    }

    public NamedOrderedGatewayFilter(int order, GatewayFilter delegate) {
        super(delegate, order);
        this.name = delegate.toString();
    }

    @Override
    public String toString() {
        return "[" + this.name + ", order = " + this.getOrder() + "]";
    }
}

