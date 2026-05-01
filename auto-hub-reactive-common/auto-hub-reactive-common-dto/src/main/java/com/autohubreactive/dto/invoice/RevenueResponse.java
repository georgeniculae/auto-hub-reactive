package com.autohubreactive.dto.invoice;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record RevenueResponse(
        String id,

        @NonNull
        LocalDate dateOfRevenue,

        @NonNull
        BigDecimal amountFromBooking
) {

    @Override
    @NonNull
    public String toString() {
        return "RevenueRequest{" + "\n" +
                "id=" + id + "\n" +
                "dateOfRevenue=" + dateOfRevenue + "\n" +
                "amountFromBooking=" + amountFromBooking + "\n" +
                "}";
    }
}
