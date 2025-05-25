package com.autohubreactive.apigateway.cache;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    public Cache<String, OpenApiInteractionValidator> caffeineCache(
            @Value("${cache.expire-after-rewrite-duration}") String duration,
            @Value("${cache.initial-capacity}") int initialCapacity,
            @Value("${cache.maximum-size}") int maximumSize
    ) {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.parse(duration))
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .build();
    }

}
