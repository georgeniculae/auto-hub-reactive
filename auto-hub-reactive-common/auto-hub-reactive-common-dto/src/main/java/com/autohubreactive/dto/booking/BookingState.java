package com.autohubreactive.dto.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookingState {

    IN_PROGRESS("In progress"),
    CLOSED("Closed");

    private final String displayName;

}
