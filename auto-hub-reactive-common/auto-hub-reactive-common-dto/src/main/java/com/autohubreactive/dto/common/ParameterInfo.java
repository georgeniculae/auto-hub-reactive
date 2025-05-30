package com.autohubreactive.dto.common;

import lombok.Builder;

@Builder
public record ParameterInfo(String parameterName, Object parameterValue) {

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "parameterName='" + parameterName + '\'' +
                ", parameterValue=" + parameterValue +
                '}';
    }
}
