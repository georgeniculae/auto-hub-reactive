package com.autohubreactive.apigateway.security;

import com.autohubreactive.apigateway.util.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class GrantedAuthoritiesConverterConfig {

    @Bean
    public Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter(JwtGrantedAuthorityConverter jwtGrantedAuthorityConverter) {
        return new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtGrantedAuthorityConverter);
    }

    @Bean
    public JwtGrantedAuthorityConverter jwtGrantedAuthorityConverter() {
        return jwt -> {
            Map<String, List<String>> claims = getClaims(jwt);

            return ObjectUtils.isEmpty(claims) ? Collections.emptyList() : getAuthorities(claims);
        };
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<String>> getClaims(Jwt source) {
        return (Map<String, List<String>>) source.getClaims().get(Constants.REALM_ACCESS);
    }

    private Collection<GrantedAuthority> getAuthorities(Map<String, List<String>> claims) {
        return claims.get(Constants.ROLES)
                .stream()
                .map(role -> new SimpleGrantedAuthority(Constants.ROLE + role))
                .collect(Collectors.toList());
    }

    public interface JwtGrantedAuthorityConverter extends Converter<Jwt, Collection<GrantedAuthority>> {
    }

}
