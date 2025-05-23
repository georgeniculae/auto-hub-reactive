package com.autohubreactive.lib.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

@UtilityClass
public class ServerRequestUtil {

    private static final String X_API_KEY = "X-API-KEY";
    private static final String X_USERNAME = "X-USERNAME";
    private static final String X_EMAIL = "X-EMAIL";
    private static final String X_ROLES = "X-ROLES";

    public static String getQueryParam(ServerRequest serverRequest, String name) {
        return serverRequest.queryParams().getFirst(name);
    }

    public static String getApiKeyHeader(ServerRequest serverRequest) {
        return serverRequest.headers().firstHeader(X_API_KEY);
    }

    public static List<String> getRolesHeader(ServerRequest serverRequest) {
        return serverRequest.headers().header(X_ROLES);
    }

    public static String getUsername(ServerRequest serverRequest) {
        return serverRequest.headers().firstHeader(X_USERNAME);
    }

    public static String getEmail(ServerRequest serverRequest) {
        return serverRequest.headers().firstHeader(X_EMAIL);
    }

    public static String getApiKeyHeader(ServerHttpRequest serverHttpRequest) {
        return serverHttpRequest.getHeaders().getFirst(X_API_KEY);
    }

    public static List<String> getRolesHeader(ServerHttpRequest serverHttpRequest) {
        return serverHttpRequest.getHeaders().get(X_ROLES);
    }

}
