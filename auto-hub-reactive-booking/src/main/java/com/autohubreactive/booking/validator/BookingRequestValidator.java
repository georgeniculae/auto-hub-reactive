package com.autohubreactive.booking.validator;

import com.autohubreactive.dto.booking.BookingRequest;
import com.autohubreactive.lib.dtovalidator.DtoConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BookingRequestValidator {

    private final DtoConstraintValidator<BookingRequest> validator;

    public final Mono<BookingRequest> validateBody(BookingRequest bookingRequest) {
        return validator.validateBody(bookingRequest);
    }

}
