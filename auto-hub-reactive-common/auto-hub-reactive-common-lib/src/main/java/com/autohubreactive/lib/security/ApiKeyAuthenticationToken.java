package com.autohubreactive.lib.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Transient
public final class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;

    public ApiKeyAuthenticationToken(List<SimpleGrantedAuthority> grantedAuthorities, String apiKey) {
        super(grantedAuthorities);
        super.setAuthenticated(false);
        this.apiKey = apiKey;
    }

    public ApiKeyAuthenticationToken(ApiKeyAuthenticationToken apiKeyAuthenticationToken) {
        super(apiKeyAuthenticationToken.getAuthorities());
        super.setAuthenticated(true);
        this.apiKey = apiKeyAuthenticationToken.getPrincipal().toString();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

}
