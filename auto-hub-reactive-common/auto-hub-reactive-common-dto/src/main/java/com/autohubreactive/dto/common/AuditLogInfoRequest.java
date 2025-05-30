package com.autohubreactive.dto.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AuditLogInfoRequest(
        @NotEmpty(message = "Method name cannot be empty")
        String methodName,

        @NotEmpty(message = "Activity description cannot be empty")
        String activityDescription,

        String username,

        @NotNull(message = "Timestamp cannot be null")
        LocalDateTime timestamp,

        List<ParameterInfo> parameters
) {

    @Override
    public String toString() {
        return "AuditLogInfoRequest{" +
                "methodName='" + methodName + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", parameters=" + parameters +
                '}';
    }

}
