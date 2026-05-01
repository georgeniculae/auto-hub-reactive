package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record CarUpdateDetails(
        @NonNull
        String carId,

        @NonNull
        CarState carState,

        @NonNull
        String receptionistEmployeeId
) {

    @Override
    @NonNull
    public String toString() {
        return "CarUpdateDetails{" + "\n" +
                "carId=" + carId + "\n" +
                "carState=" + carState + "\n" +
                "returnRentalOfficeId=" + receptionistEmployeeId + "\n" +
                "}";
    }

}
