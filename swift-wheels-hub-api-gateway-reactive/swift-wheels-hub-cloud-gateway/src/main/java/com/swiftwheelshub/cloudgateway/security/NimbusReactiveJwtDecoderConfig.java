package com.swiftwheelshub.cloudgateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

@Configuration
public class NimbusReactiveJwtDecoderConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkUri;

    @Bean
    public NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkUri).build();
    }

}
