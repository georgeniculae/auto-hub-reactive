package com.autohubreactive.booking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookingStatus {

    IN_PROGRESS("In progress"),
    CLOSED("Closed");

    private final String displayName;

}
