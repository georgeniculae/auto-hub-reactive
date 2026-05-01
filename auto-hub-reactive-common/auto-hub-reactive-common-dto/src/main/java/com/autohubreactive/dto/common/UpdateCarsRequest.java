package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record UpdateCarsRequest(
        @NonNull
        String previousCarId,

        @NonNull
        String actualCarId
) {

    @Override
    @NonNull
    public String toString() {
        return "UpdateCarsRequest{" + "\n" +
                "previousCarId=" + previousCarId + "\n" +
                "actualCarId=" + actualCarId + "\n" +
                "}";
    }

}
