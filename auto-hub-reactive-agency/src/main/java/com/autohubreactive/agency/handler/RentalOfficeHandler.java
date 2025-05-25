package com.autohubreactive.agency.handler;

import com.autohubreactive.agency.service.RentalOfficeService;
import com.autohubreactive.agency.util.Constants;
import com.autohubreactive.agency.validator.RentalOfficeRequestValidator;
import com.autohubreactive.dto.agency.RentalOfficeRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RentalOfficeHandler {

    private final RentalOfficeService rentalOfficeService;
    private final RentalOfficeRequestValidator rentalOfficeRequestValidator;

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> findAllRentalOffices(ServerRequest serverRequest) {
        return rentalOfficeService.findAllRentalOffices()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(rentalOfficeResponses -> ServerResponse.ok().bodyValue(rentalOfficeResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findRentalOfficeById(ServerRequest serverRequest) {
        return rentalOfficeService.findRentalOfficeById(serverRequest.pathVariable(Constants.ID))
                .flatMap(rentalOfficeResponse -> ServerResponse.ok().bodyValue(rentalOfficeResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> findRentalOfficesByFilterInsensitiveCase(ServerRequest serverRequest) {
        return rentalOfficeService.findRentalOfficesByFilterInsensitiveCase(serverRequest.pathVariable(Constants.FILTER))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(rentalOfficeResponses -> ServerResponse.ok().bodyValue(rentalOfficeResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> countRentalOffices(ServerRequest serverRequest) {
        return rentalOfficeService.countRentalOffices()
                .flatMap(numberOfRentalOffices -> ServerResponse.ok().bodyValue(numberOfRentalOffices));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> saveRentalOffice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RentalOfficeRequest.class)
                .flatMap(rentalOfficeRequestValidator::validateBody)
                .flatMap(rentalOfficeService::saveRentalOffice)
                .flatMap(rentalOfficeResponse -> ServerResponse.ok().bodyValue(rentalOfficeResponse));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> updateRentalOffice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RentalOfficeRequest.class)
                .flatMap(rentalOfficeRequestValidator::validateBody)
                .flatMap(rentalOfficeRequest -> rentalOfficeService.updateRentalOffice(
                                serverRequest.pathVariable(Constants.ID),
                                rentalOfficeRequest
                        )
                )
                .flatMap(rentalOfficeResponse -> ServerResponse.ok().bodyValue(rentalOfficeResponse));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> deleteRentalOfficeById(ServerRequest serverRequest) {
        return rentalOfficeService.deleteRentalOfficeById(serverRequest.pathVariable(Constants.ID))
                .then(ServerResponse.noContent().build());
    }

}
