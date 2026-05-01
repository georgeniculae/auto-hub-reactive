package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record ParameterInfo(String parameterName, Object parameterValue) {

    @Override
    @NonNull
    public String toString() {
        return "ParameterInfo{" +
                "parameterName='" + parameterName + '\'' +
                ", parameterValue=" + parameterValue +
                '}';
    }
}
