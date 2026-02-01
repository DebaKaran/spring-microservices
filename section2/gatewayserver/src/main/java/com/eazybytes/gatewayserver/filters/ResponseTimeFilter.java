package com.eazybytes.gatewayserver.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseTimeFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "requestStartTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(START_TIME);
                    if (startTime != null) {
                        long duration = System.currentTimeMillis() - startTime;
                        ServerHttpResponse response = exchange.getResponse();
                        response.getHeaders().add("Response-Time", duration + "ms");
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

