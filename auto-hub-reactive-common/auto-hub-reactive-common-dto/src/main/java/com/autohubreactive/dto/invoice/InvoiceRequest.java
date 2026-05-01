package com.autohubreactive.dto.invoice;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InvoiceRequest(
        @NonNull
        String receptionistEmployeeId,

        @NonNull
        String returnRentalOfficeId,

        @NonNull
        String bookingId,

        LocalDate carReturnDate,

        @NonNull
        Boolean isVehicleDamaged,

        BigDecimal damageCost,

        BigDecimal additionalPayment,

        String comments
) {

    @Override
    @NonNull
    public String toString() {
        return "InvoiceRequest{" + "\n" +
                "returnRentalOfficeId=" + receptionistEmployeeId + "\n" +
                "bookingId=" + bookingId + "\n" +
                "carReturnDate=" + carReturnDate + "\n" +
                "isVehicleDamaged=" + isVehicleDamaged + "\n" +
                "damageCost=" + damageCost + "\n" +
                "additionalPayment=" + additionalPayment + "\n" +
                "comments='" + comments + "\n" +
                "}";
    }

}
