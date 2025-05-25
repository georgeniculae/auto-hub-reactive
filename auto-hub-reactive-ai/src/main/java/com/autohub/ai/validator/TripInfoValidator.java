package com.autohub.ai.validator;

import com.autohubreactive.dto.ai.TripInfo;
import com.autohubreactive.lib.dtovalidator.DtoConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TripInfoValidator {

    private final DtoConstraintValidator<TripInfo> dtoConstraintValidator;

    public Mono<TripInfo> validateBody(TripInfo tripInfo) {
        return dtoConstraintValidator.validateBody(tripInfo);
    }

}
