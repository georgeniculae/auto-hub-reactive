package com.autohubreactive.apigateway.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@TestConfiguration
public class TestConfig {

    @Bean
    public Cache<String, OpenApiInteractionValidator> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.parse("PT23H59M59.999S"))
                .initialCapacity(10)
                .maximumSize(50)
                .build();
    }

}
