package com.autohubreactive.lib.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class WebClientUtil {

    public static Consumer<HttpHeaders> setHttpHeaders(String apiKey, List<String> roles) {
        return httpHeaders -> {
            httpHeaders.add(Constants.X_API_KEY, apiKey);
            httpHeaders.addAll(Constants.X_ROLES, roles);
        };
    }

}
