package com.eazybytes.gatewayserver.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Component
public class GatewayTimeFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    exchange.getResponse()
                            .getHeaders()
                            .add("Gateway-Time", OffsetDateTime.now().toString());
                })
        );
    }
}
