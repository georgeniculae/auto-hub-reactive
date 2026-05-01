package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record CarStatusUpdate(
        @NonNull
        String carId,

        @NonNull
        CarState carState
) {

    @Override
    @NonNull
    public String toString() {
        return "CarStatusUpdate{" + "\n" +
                "previousCarId=" + carId + "\n" +
                "carState=" + carState + "\n" +
                "}";
    }

}
