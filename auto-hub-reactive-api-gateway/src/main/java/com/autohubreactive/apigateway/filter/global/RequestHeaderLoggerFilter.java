package com.autohubreactive.apigateway.filter.global;

import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestHeaderLoggerFilter implements GlobalFilter, Ordered {

    private static final String X_API_KEY = "X-API-KEY";
    private static final String X_EMAIL = "X-EMAIL";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(exchange)
                .map(webExchange -> webExchange.getRequest().getHeaders())
                .doOnNext(this::logHeaders)
                .flatMap(_ -> chain.filter(exchange))
                .onErrorResume(e -> {
                    log.error("Error while trying to log headers: {}", e.getMessage());

                    HttpStatusCode statusCode = ExceptionUtil.extractExceptionStatusCode(e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(statusCode);

                    return response.setComplete();
                });
    }

    @Override
    public int getOrder() {
        return 2;
    }

    private void logHeaders(HttpHeaders httpHeaders) {
        log.info("Request headers: ");

        httpHeaders.forEach((header, value) -> {
            if (!X_API_KEY.equals(header) && !X_EMAIL.equals(header)) {
                log.info("{}: {}", header, value);
            }
        });
    }

}
