package com.autohubreactive.apigateway.filter.global;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.report.ValidationReport;
import com.autohubreactive.apigateway.cache.OpenApiCache;
import com.autohubreactive.apigateway.util.Constants;
import com.autohubreactive.exception.AutoHubResponseStatusException;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestValidatorFilter implements GlobalFilter, Ordered {

    private final OpenApiCache openApiCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(exchange)
                .flatMap(serverWebExchange -> forwardRequest(chain, serverWebExchange))
                .onErrorResume(e -> {
                    log.error("Error while validating request: {}", e.getMessage());

                    HttpStatusCode statusCode = ExceptionUtil.extractExceptionStatusCode(e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(statusCode);

                    return response.setComplete();
                });
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private Mono<Void> forwardRequest(GatewayFilterChain chain, ServerWebExchange serverWebExchange) {
        if (isRequestValidatable(serverWebExchange.getRequest())) {
            return filterValidatedRequest(serverWebExchange, chain);
        }

        return chain.filter(serverWebExchange);
    }

    private boolean isRequestValidatable(ServerHttpRequest serverHttpRequest) {
        String path = serverHttpRequest.getPath().value();

        return !path.contains(Constants.DEFINITION) && !path.contains(Constants.ACTUATOR) && !path.contains(Constants.FALLBACK);
    }

    private Mono<Void> filterValidatedRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        return getSimpleRequest(exchange.getRequest())
                .flatMap(simpleRequest -> validateRequest(exchange, chain, simpleRequest));
    }

    private Mono<Void> validateRequest(ServerWebExchange exchange, GatewayFilterChain chain, SimpleRequest simpleRequest) {
        String microserviceIdentifier = getMicroserviceIdentifier(simpleRequest);
        OpenApiInteractionValidator openApiInteractionValidator = openApiCache.get(microserviceIdentifier);
        ValidationReport validationReport = openApiInteractionValidator.validateRequest(simpleRequest);
        String validationErrorMessage = getValidationErrorMessage(validationReport);

        return validateResponse(exchange, chain, validationErrorMessage);
    }

    private String getMicroserviceIdentifier(SimpleRequest simpleRequest) {
        return simpleRequest.getPath()
                .replaceFirst(Constants.SEPARATOR_REGEX, StringUtils.EMPTY)
                .split(Constants.SEPARATOR_REGEX)[0];
    }

    private Mono<SimpleRequest> getSimpleRequest(ServerHttpRequest request) {
        return request.getBody()
                .map(dataBuffer -> dataBuffer.toString(StandardCharsets.UTF_8))
                .reduce(StringUtils.EMPTY, (current, next) -> current + next)
                .map(bodyAsString -> buildSimpleRequest(request, bodyAsString));
    }

    private SimpleRequest buildSimpleRequest(ServerHttpRequest request, String bodyAsString) {
        SimpleRequest.Builder simpleRequestBuilder = new SimpleRequest.Builder(request.getMethod().name(), request.getPath().value());

        request.getHeaders()
                .forEach(simpleRequestBuilder::withHeader);

        request.getQueryParams()
                .forEach(simpleRequestBuilder::withQueryParam);

        simpleRequestBuilder.withBody(bodyAsString);

        return simpleRequestBuilder.build();
    }

    private String getValidationErrorMessage(ValidationReport validationReport) {
        return validationReport.getMessages()
                .stream()
                .filter(message -> ValidationReport.Level.IGNORE != message.getLevel())
                .map(ValidationReport.Message::getMessage)
                .collect(Collectors.joining());
    }

    private Mono<Void> validateResponse(ServerWebExchange exchange, GatewayFilterChain chain, String validationMessage) {
        if (ObjectUtils.isEmpty(validationMessage)) {
            return chain.filter(exchange);
        }

        return Mono.error(new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, validationMessage));
    }

}
