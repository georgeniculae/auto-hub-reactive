package com.autohubreactive.dto.common;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InvoiceResponse(
        String id,

        @NonNull
        String customerUsername,

        @NonNull
        String customerEmail,

        @NonNull
        String carId,

        String receptionistEmployeeId,

        String returnRentalOfficeId,

        @NonNull
        String bookingId,

        LocalDate carReturnDate,

        LocalDate dateTo,

        LocalDate dateFrom,

        Boolean isVehicleDamaged,

        BigDecimal damageCost,

        BigDecimal additionalPayment,

        BigDecimal totalAmount,

        BigDecimal rentalCarPrice,

        String comments
) {

    @Override
    @NonNull
    public String toString() {
        return "InvoiceResponse{" +
                "id='" + id + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", carId='" + carId + '\'' +
                ", receptionistEmployeeId='" + receptionistEmployeeId + '\'' +
                ", returnRentalOfficeId='" + returnRentalOfficeId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", carReturnDate=" + carReturnDate +
                ", dateTo=" + dateTo +
                ", dateFrom=" + dateFrom +
                ", isVehicleDamaged=" + isVehicleDamaged +
                ", damageCost=" + damageCost +
                ", additionalPayment=" + additionalPayment +
                ", totalAmount=" + totalAmount +
                ", rentalCarPrice=" + rentalCarPrice +
                ", comments='" + comments +
                '}';
    }

}
