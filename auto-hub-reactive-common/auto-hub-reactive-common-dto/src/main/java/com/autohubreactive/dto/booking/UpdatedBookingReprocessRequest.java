package com.autohubreactive.dto.booking;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UpdatedBookingReprocessRequest(
        String id,

        @NonNull
        LocalDate dateOfBooking,

        BookingState status,

        String customerUsername,

        String customerEmail,

        @NonNull
        String actualCarId,

        String previousCarId,

        @NonNull
        LocalDate dateFrom,

        @NonNull
        LocalDate dateTo,

        BigDecimal rentalCarPrice,

        String pickupRentalOfficeId,

        String returnRentalOfficeId,

        boolean isCarChanged
) {
}
