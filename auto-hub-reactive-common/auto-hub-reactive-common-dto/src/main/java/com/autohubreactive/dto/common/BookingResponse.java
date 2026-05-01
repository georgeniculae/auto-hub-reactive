package com.autohubreactive.dto.common;

import com.autohubreactive.dto.booking.BookingState;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record BookingResponse(
        String id,

        @NonNull
        LocalDate dateOfBooking,

        BookingState status,

        @NonNull
        String customerUsername,

        @NonNull
        String customerEmail,

        @NonNull
        String carId,

        @NonNull
        LocalDate dateFrom,

        @NonNull
        LocalDate dateTo,

        BigDecimal rentalCarPrice,

        @NonNull
        String pickupRentalOfficeId,

        String returnRentalOfficeId
) {

    @Override
    @NonNull
    public String toString() {
        return "BookingResponse{" + "\n" +
                "id=" + id + "\n" +
                "dateOfBooking=" + dateOfBooking + "\n" +
                "status=" + status + "\n" +
                "customerUsername=" + customerUsername + "\n" +
                "carId=" + carId + "\n" +
                "dateFrom=" + dateFrom + "\n" +
                "dateTo=" + dateTo + "\n" +
                "rentalCarPrice=" + rentalCarPrice + "\n" +
                "pickupRentalOfficeId=" + pickupRentalOfficeId + "\n" +
                "returnRentalOfficeId=" + returnRentalOfficeId + "\n" +
                "}";
    }

}
