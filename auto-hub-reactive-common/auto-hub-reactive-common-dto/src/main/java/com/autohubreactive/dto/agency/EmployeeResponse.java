package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record EmployeeResponse(
        String id,

        @NonNull
        String firstName,

        @NonNull
        String lastName,

        @NonNull
        String jobPosition,

        @NonNull
        String workingBranchId
) {

    @Override
    public String toString() {
        return "EmployeeResponse{" + "\n" +
                "id=" + id + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "jobPosition=" + jobPosition + "\n" +
                "workingBranchId=" + workingBranchId + "\n" +
                "}";
    }

}
