package com.autohubreactive.apigateway.security;

import com.autohubreactive.apigateway.util.Constants;
import com.autohubreactive.exception.AutoHubException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenConverter {

    private final Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    public String extractUsername(Jwt source) {
        return Optional.ofNullable(source.getClaims().get(Constants.USERNAME_CLAIM))
                .map(String::valueOf)
                .orElseThrow(() -> new AutoHubException("Username claim is null"));
    }

    public String extractEmail(Jwt source) {
        return Optional.ofNullable(source.getClaims().get(Constants.EMAIL_CLAIM))
                .map(String::valueOf)
                .orElseThrow(() -> new AutoHubException("Email claim is null"));
    }

    public Flux<GrantedAuthority> extractGrantedAuthorities(Jwt source) {
        return jwtGrantedAuthoritiesConverter.convert(source);
    }

}
