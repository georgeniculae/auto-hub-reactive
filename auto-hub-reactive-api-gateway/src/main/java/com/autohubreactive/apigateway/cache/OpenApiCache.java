package com.autohubreactive.apigateway.cache;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class OpenApiCache {

    private final Cache<String, OpenApiInteractionValidator> caffeineCache;

    public OpenApiInteractionValidator get(String serviceId) {
        return caffeineCache.getIfPresent(serviceId);
    }

    public void put(String identifier, OpenApiInteractionValidator interactionValidator) {
        caffeineCache.put(identifier, interactionValidator);
    }

    public ConcurrentMap<String, OpenApiInteractionValidator> toMap() {
        return caffeineCache.asMap();
    }

}
