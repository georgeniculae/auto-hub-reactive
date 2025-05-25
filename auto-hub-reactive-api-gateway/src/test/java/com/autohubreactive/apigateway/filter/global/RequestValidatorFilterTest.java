package com.autohubreactive.apigateway.filter.global;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.autohubreactive.apigateway.cache.OpenApiCache;
import com.autohubreactive.apigateway.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestValidatorFilterTest {

    @InjectMocks
    private RequestValidatorFilter requestValidatorFilter;

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private OpenApiCache openApiCache;

    @Test
    void filterTest_success() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/agency/rental-offices/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ServerWebExchange exchange = MockServerWebExchange.builder(request).build();

        String agencyContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveAgencySwagger.json", String.class);

        OpenApiInteractionValidator validator =
                OpenApiInteractionValidator.createForInlineApiSpecification(agencyContent).build();

        when(openApiCache.get(anyString())).thenReturn(validator);
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        requestValidatorFilter.filter(exchange, chain)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void filterTest_validationReportWithErrors() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/agency/rental-offices/", 1)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ServerWebExchange exchange = MockServerWebExchange.builder(request).build();

        String agencyContent =
                TestUtil.getResourceAsJson("/data/AutoHubReactiveAgencySwagger.json", String.class);

        OpenApiInteractionValidator validator =
                OpenApiInteractionValidator.createForInlineApiSpecification(agencyContent).build();

        when(openApiCache.get(anyString())).thenReturn(validator);

        requestValidatorFilter.filter(exchange, chain)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void filterTest_definitionPath() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/agency/definition/swagger-ui.html")
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ServerWebExchange exchange = MockServerWebExchange.builder(request).build();

        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        requestValidatorFilter.filter(exchange, chain)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
