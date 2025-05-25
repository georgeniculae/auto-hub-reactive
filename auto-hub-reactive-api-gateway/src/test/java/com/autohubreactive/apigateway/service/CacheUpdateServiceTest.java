package com.autohubreactive.apigateway.service;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.autohubreactive.apigateway.cache.OpenApiCache;
import com.autohubreactive.apigateway.property.RegisteredEndpoints;
import com.autohubreactive.apigateway.util.TestUtil;
import com.autohubreactive.lib.retry.RetryHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheUpdateServiceTest {

    private static String agencyContent;
    private static String aiContent;
    private static String bookingsContent;
    private static String customersContent;
    private static String expenseContent;
    private static List<RegisteredEndpoints.RegisteredEndpoint> endpoints;

    @InjectMocks
    private CacheUpdateService cacheUpdateService;

    @Mock
    private OpenApiCache openApiCache;

    @Mock
    private WebClient webClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private RegisteredEndpoints registeredEndpoints;

    @Mock
    private RetryHandler retryHandler;

    @BeforeAll
    static void init() {
        agencyContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveAgencySwagger.json", String.class);
        aiContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveAiSwagger.json", String.class);
        bookingsContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveBookingsSwagger.json", String.class);
        customersContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveCustomersSwagger.json", String.class);
        expenseContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveExpenseSwagger.json", String.class);

        endpoints = List.of(
                new RegisteredEndpoints.RegisteredEndpoint("agency", "agency-url"),
                new RegisteredEndpoints.RegisteredEndpoint("ai", "ai-url"),
                new RegisteredEndpoints.RegisteredEndpoint("bookings", "bookings-url"),
                new RegisteredEndpoints.RegisteredEndpoint("customers", "customers-url"),
                new RegisteredEndpoints.RegisteredEndpoint("expense", "expense-url")
        );
    }

    @BeforeEach
    void setUp() {
        when(retryHandler.retry()).thenReturn(Retry.fixedDelay(3, Duration.ZERO));
    }

    @Test
    @SuppressWarnings("all")
    void populateCache_success() {
        when(registeredEndpoints.getEndpoints()).thenReturn(endpoints);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                count++;

                if (count == 1) {
                    return Mono.just(agencyContent);
                } else if (count == 2) {
                    return Mono.just(aiContent);
                } else if (count == 3) {
                    return Mono.just(bookingsContent);
                } else if (count == 4) {
                    return Mono.just(customersContent);
                } else {
                    return Mono.just(expenseContent);
                }
            }
        });
        when(openApiCache.toMap()).thenReturn(new ConcurrentHashMap<>());

        cacheUpdateService.populateCache()
                .as(StepVerifier::create)
                .expectNextCount(5)
                .verifyComplete();

        verify(openApiCache).put(eq("agency"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("ai"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("bookings"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("customers"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("expense"), any(OpenApiInteractionValidator.class));
    }

    @Test
    @SuppressWarnings("all")
    void populateCache_emptySwaggerContent() {
        when(registeredEndpoints.getEndpoints()).thenReturn(endpoints);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                count++;

                if (count == 1) {
                    return Mono.just(agencyContent);
                } else if (count == 2) {
                    return Mono.just(aiContent);
                } else if (count == 3) {
                    return Mono.just(bookingsContent);
                } else if (count == 4) {
                    return Mono.just(customersContent);
                } else {
                    return Mono.empty();
                }
            }
        });
        when(openApiCache.toMap()).thenReturn(new ConcurrentHashMap<>());

        cacheUpdateService.populateCache()
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();

        verify(openApiCache).put(eq("agency"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("ai"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("bookings"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("customers"), any(OpenApiInteractionValidator.class));
    }

    @Test
    @SuppressWarnings("all")
    void populateCache_errorOnGettingSwagger() {
        when(registeredEndpoints.getEndpoints()).thenReturn(endpoints);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                count++;

                if (count == 1) {
                    return Mono.just(agencyContent);
                } else if (count == 2) {
                    return Mono.just(aiContent);
                } else if (count == 3) {
                    return Mono.just(bookingsContent);
                } else if (count == 4) {
                    return Mono.just(customersContent);
                } else {
                    return Mono.error(new RuntimeException("Test"));
                }
            }
        });
        when(openApiCache.toMap()).thenReturn(new ConcurrentHashMap<>());

        cacheUpdateService.populateCache()
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();

        verify(openApiCache).put(eq("agency"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("ai"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("bookings"), any(OpenApiInteractionValidator.class));
        verify(openApiCache).put(eq("customers"), any(OpenApiInteractionValidator.class));
    }

}
