package com.autohubreactive.dto.booking;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Builder
public record BookingRequest(
        @NonNull
        LocalDate dateOfBooking,

        @NonNull
        String carId,

        @NonNull
        LocalDate dateFrom,

        @NonNull
        LocalDate dateTo,

        @NonNull
        String pickupRentalOfficeId
) {

    @Override
    @NonNull
    public String toString() {
        return "BookingRequest{" + "\n" +
                "dateOfBooking=" + dateOfBooking + "\n" +
                "carId=" + carId + "\n" +
                "dateFrom=" + dateFrom + "\n" +
                "dateTo=" + dateTo + "\n" +
                "pickupRentalOfficeId=" + pickupRentalOfficeId + "\n" +
                "}";
    }

}
