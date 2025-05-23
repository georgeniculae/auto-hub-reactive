package com.autohubreactive.dto.common;

import lombok.Builder;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AuditLogInfoRequest(
        @NonNull
        String methodName,

        String username,

        @NonNull
        LocalDateTime timestamp,

        List<String> parametersValues
) {

    @Override
    public String toString() {
        return "AuditLogInfoRequest{" + "\n" +
                "methodName=" + methodName + "\n" +
                "username=" + username + "\n" +
                "timestamp=" + timestamp + "\n" +
                "parametersValues=" + parametersValues + "\n" +
                "}";
    }

}
