package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String correlationId = filterUtility.getCorrelationId(headers);

        if (correlationId == null) {
            correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug("eazybank-correlation-id generated in RequestTraceFilter: {}", correlationId);
        }
        final String finalCorrelationId = correlationId;
        return chain.filter(exchange)
                .contextWrite(ctx ->
                        ctx.put(FilterUtility.CORRELATION_ID, finalCorrelationId));
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
