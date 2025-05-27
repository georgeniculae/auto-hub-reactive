package com.autohubreactive.apigateway.filter.factory;

import com.autohubreactive.apigateway.util.Constants;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestTraceGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTraceGatewayFilterFactory.ServiceIdConfig> {

    public RequestTraceGatewayFilterFactory() {
        super(ServiceIdConfig.class);
    }

    @Override
    public GatewayFilter apply(ServiceIdConfig serviceIdConfig) {
        return (exchange, chain) -> Mono.just(createMutatedServerWebExchange(serviceIdConfig, exchange))
                .flatMap(chain::filter)
                .onErrorResume(e -> {
                    log.error("Error while trying to add request tracing information: {}", e.getMessage());

                    HttpStatusCode statusCode = ExceptionUtil.extractExceptionStatusCode(e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(statusCode);

                    return response.setComplete();
                });
    }

    private ServerWebExchange createMutatedServerWebExchange(
            ServiceIdConfig serviceIdConfig,
            ServerWebExchange exchange
    ) {
        return exchange.mutate()
                .request(requestBuilder -> {
                    requestBuilder.header(Constants.X_SERVICE_ID, serviceIdConfig.getServiceId());
                    requestBuilder.header(Constants.X_PATH, exchange.getRequest().getURI().getPath());
                })
                .build();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ServiceIdConfig {

        private String serviceId;

    }

}
