package com.autohubreactive.agency.validator;

import com.autohubreactive.dto.agency.RentalOfficeRequest;
import com.autohubreactive.lib.dtovalidator.DtoConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RentalOfficeRequestValidator {

    private final DtoConstraintValidator<RentalOfficeRequest> validator;

    public final Mono<RentalOfficeRequest> validateBody(RentalOfficeRequest rentalOfficeRequest) {
        return validator.validateBody(rentalOfficeRequest);
    }

}
