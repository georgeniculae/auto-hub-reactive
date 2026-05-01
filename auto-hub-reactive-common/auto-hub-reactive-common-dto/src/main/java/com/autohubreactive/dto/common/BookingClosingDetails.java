package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record BookingClosingDetails(
        @NonNull
        String bookingId,

        @NonNull
        String returnRentalOfficeId
) {

    @Override
    @NonNull
    public String toString() {
        return "BookingClosingDetails{" + "\n" +
                "bookingId=" + bookingId + "\n" +
                "returnRentalOfficeId=" + returnRentalOfficeId + "\n" +
                "}";
    }

}
