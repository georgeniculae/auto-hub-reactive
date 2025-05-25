package com.autohubreactive.agency.validator;

import com.autohubreactive.dto.agency.EmployeeRequest;
import com.autohubreactive.lib.dtovalidator.DtoConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmployeeRequestValidator {

    private final DtoConstraintValidator<EmployeeRequest> validator;

    public final Mono<EmployeeRequest> validateBody(EmployeeRequest employeeRequest) {
        return validator.validateBody(employeeRequest);
    }

}
