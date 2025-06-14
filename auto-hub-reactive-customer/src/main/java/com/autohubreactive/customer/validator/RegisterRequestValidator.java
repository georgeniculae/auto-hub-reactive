package com.autohubreactive.customer.validator;

import com.autohubreactive.dto.customer.RegisterRequest;
import com.autohubreactive.lib.dtovalidator.DtoConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RegisterRequestValidator {

    private final DtoConstraintValidator<RegisterRequest> validator;

    public Mono<RegisterRequest> validateBody(RegisterRequest registerRequest) {
        return validator.validateBody(registerRequest);
    }

}
