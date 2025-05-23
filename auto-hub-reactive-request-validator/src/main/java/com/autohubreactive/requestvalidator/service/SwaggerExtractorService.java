package com.autohubreactive.requestvalidator.service;

import com.autohubreactive.exception.AutoHubException;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import com.autohubreactive.lib.retry.RetryHandler;
import com.autohubreactive.requestvalidator.config.RegisteredEndpoints;
import com.autohubreactive.requestvalidator.model.SwaggerFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SwaggerExtractorService {

    private final WebClient webClient;
    private final RegisteredEndpoints registeredEndpoints;
    private final RetryHandler retryHandler;

    public Flux<SwaggerFile> getSwaggerFiles() {
        return Flux.fromIterable(registeredEndpoints.getEndpoints())
                .flatMap(this::createSwaggerFile);
    }

    public Mono<SwaggerFile> getSwaggerFileForMicroservice(String microserviceName) {
        return getSwaggerFiles()
                .filter(swaggerFile -> microserviceName.contains(swaggerFile.getIdentifier()))
                .switchIfEmpty(Mono.error(new AutoHubException("Microservice " + microserviceName + " not existent")))
                .next();
    }

    private Mono<SwaggerFile> createSwaggerFile(RegisteredEndpoints.RegisteredEndpoint endpoint) {
        return getSwaggerContent(endpoint.getIdentifier(), endpoint.getUrl())
                .map(swaggerContent -> getSwaggerFile(endpoint, swaggerContent));
    }

    private Mono<String> getSwaggerContent(String identifier, String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(retryHandler.retry())
                .filter(StringUtils::isNotBlank)
                .switchIfEmpty(Mono.error(new AutoHubException("Swagger for: " + identifier + " is empty")))
                .onErrorMap(ExceptionUtil::handleException);
    }

    private SwaggerFile getSwaggerFile(RegisteredEndpoints.RegisteredEndpoint endpoint, String swaggerContent) {
        return SwaggerFile.builder()
                .identifier(endpoint.getIdentifier())
                .swaggerContent(swaggerContent)
                .build();
    }

}
