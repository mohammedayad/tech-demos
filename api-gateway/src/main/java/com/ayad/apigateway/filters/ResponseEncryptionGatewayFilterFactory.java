package com.ayad.apigateway.filters;


import io.micrometer.observation.Observation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ayad.apigateway.common.utils.FilterConstants.*;


// first post filter
@Slf4j
@Component
public class ResponseEncryptionGatewayFilterFactory extends AbstractGatewayFilterFactory<ResponseEncryptionGatewayFilterFactory.Config> {

    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory;

    public ResponseEncryptionGatewayFilterFactory(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory) {
        super(ResponseEncryptionGatewayFilterFactory.Config.class);
        this.modifyResponseBodyFilterFactory = modifyResponseBodyFilterFactory;
    }

    @Override
    public GatewayFilter apply(Config config) {
//        final ModifyResponseBodyGatewayFilterFactory.Config modifyResponseBodyFilterFactoryConfig = new ModifyResponseBodyGatewayFilterFactory.Config();
//        GatewayFilter modifyResponseBodyFilter = modifyResponseBodyFilterFactory.apply(modifyResponseBodyFilterFactoryConfig);
//        log.info(config.toString());
//        return new NamedOrderedGatewayFilter(getOrder(), (exchange, chain) -> chain.filter(exchange));

        final ModifyResponseBodyGatewayFilterFactory.Config modifyResponseBodyFilterFactoryConfig = new ModifyResponseBodyGatewayFilterFactory.Config();
        modifyResponseBodyFilterFactoryConfig.setRewriteFunction(byte[].class, byte[].class, (exchange, originalBody) -> {
            byte[] newBody = originalBody;
            if(shouldFilter(exchange)) {
                log.info("TraceId: [{}], SpanId: [{}]", MDC.get("traceId"), MDC.get("spanId"));
                Observation gatewayObservation = exchange
                        .getAttribute(ServerWebExchangeUtils.GATEWAY_OBSERVATION_ATTR);
                // org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gateway.observation
                ServerRequestObservationContext serverRequestObservationContext = exchange
                        .getAttribute("org.springframework.http.server.reactive.observation.ServerRequestObservationContext");

                log.debug("ResponseEncryption filter started");

                try {

                    newBody = "hello there".getBytes();

                } catch (Exception ex) {
                    log.info("Caught exception when encrypting", ex);
                    return Mono.error(ex);
                }
            }
            return Mono.justOrEmpty(newBody);
        });

        GatewayFilter modifyResponseBodyFilter = modifyResponseBodyFilterFactory.apply(modifyResponseBodyFilterFactoryConfig);
        return new NamedOrderedGatewayFilter(getOrder(), modifyResponseBodyFilter);
    }


    public boolean shouldFilter(ServerWebExchange exchange) {
       return true;
    }


    public int getOrder() {
        return RESPONSE_ENCRYPTION_FILTER_ORDER;
    }

    public static class Config {
        // no config
    }
}
