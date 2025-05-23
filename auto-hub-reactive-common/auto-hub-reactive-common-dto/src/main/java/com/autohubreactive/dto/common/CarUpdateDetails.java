package com.autohubreactive.dto.common;

import lombok.Builder;
import org.springframework.lang.NonNull;

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
    public String toString() {
        return "CarUpdateDetails{" + "\n" +
                "carId=" + carId + "\n" +
                "carState=" + carState + "\n" +
                "returnBranchId=" + receptionistEmployeeId + "\n" +
                "}";
    }

}
