package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

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
        String workingRentalOfficeId
) {

    @Override
    @NonNull
    public String toString() {
        return "EmployeeResponse{" + "\n" +
                "id=" + id + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "jobPosition=" + jobPosition + "\n" +
                "workingRentalOfficeId=" + workingRentalOfficeId + "\n" +
                "}";
    }

}
