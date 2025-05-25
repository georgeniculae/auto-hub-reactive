package com.autohubreactive.apigateway.cache;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.whitelist.ValidationErrorsWhitelist;
import com.atlassian.oai.validator.whitelist.rule.WhitelistRules;
import com.autohubreactive.apigateway.util.Constants;
import com.autohubreactive.lib.retry.RetryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheUpdateService {

    private final OpenApiCache openApiCache;
    private final WebClient webClient;
    private final RetryHandler retryHandler;
    private final RegisteredEndpoints registeredEndpoints;

    public Flux<Swagger> populateCache() {
        return Flux.fromIterable(registeredEndpoints.getEndpoints())
                .flatMap(this::getSwagger)
                .doOnNext(swagger -> openApiCache.put(swagger.identifier(), swagger.swaggerValidator()))
                .doOnNext(swagger -> log.info("Loaded Swagger for: {}", swagger.identifier()))
                .doOnComplete(() -> log.info("Cache update complete, cached {} specs", openApiCache.toMap().size()));
    }

    private Mono<Swagger> getSwagger(RegisteredEndpoints.RegisteredEndpoint registeredEndpoint) {
        return webClient.get()
                .uri(registeredEndpoint.getUrl())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(retryHandler.retry())
                .filter(StringUtils::isNotBlank)
                .map(swaggerContent -> createSwaggerObject(registeredEndpoint, swaggerContent))
                .onErrorResume(e -> {
                    log.error("Error getting swagger for {}", registeredEndpoint.getIdentifier(), e);

                    return Mono.empty();
                });
    }

    private Swagger createSwaggerObject(RegisteredEndpoints.RegisteredEndpoint registeredEndpoint, String swaggerContent) {
        return Swagger.builder()
                .identifier(registeredEndpoint.getIdentifier())
                .swaggerValidator(getOpenApiInteractionValidator(swaggerContent))
                .build();
    }

    private OpenApiInteractionValidator getOpenApiInteractionValidator(String swaggerContent) {
        return OpenApiInteractionValidator.createForInlineApiSpecification(swaggerContent)
                .withWhitelist(getWhitelist())
                .build();
    }

    private ValidationErrorsWhitelist getWhitelist() {
        return ValidationErrorsWhitelist.create()
                .withRule(Constants.SWAGGER_PATH, WhitelistRules.pathContainsSubstring(Constants.SWAGGER))
                .withRule(Constants.SWAGGER_MESSAGE, WhitelistRules.messageContainsSubstring(Constants.SWAGGER))
                .withRule(Constants.V3_PATH, WhitelistRules.pathContainsSubstring(Constants.V3))
                .withRule(Constants.V3_MESSAGE, WhitelistRules.messageContainsSubstring(Constants.V3))
                .withRule(Constants.ACTUATOR, WhitelistRules.pathContainsSubstring(Constants.ACTUATOR));
    }

}
