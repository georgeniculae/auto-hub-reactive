package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record EmployeeRequest(
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
        return "EmployeeRequest{" + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "jobPosition=" + jobPosition + "\n" +
                "workingRentalOfficeId=" + workingRentalOfficeId + "\n" +
                "}";
    }

}
