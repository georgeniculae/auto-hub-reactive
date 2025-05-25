package com.autohubreactive.apigateway.cache;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import lombok.Builder;

@Builder
public record Swagger(String identifier, OpenApiInteractionValidator swaggerValidator) {
}
