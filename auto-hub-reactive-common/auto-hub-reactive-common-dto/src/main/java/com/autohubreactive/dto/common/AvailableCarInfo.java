package com.autohubreactive.dto.common;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AvailableCarInfo(
        String id,
        String actualRentalOfficeId,
        BigDecimal amount
) {
}
