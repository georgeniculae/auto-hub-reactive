package com.autohubreactive.agency.handler;

import com.autohubreactive.agency.service.CarService;
import com.autohubreactive.dto.agency.CarResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CarHandler {

    private static final String ID = "id";
    private static final String MAKE = "make";
    private static final String FILTER = "filter";
    private static final String FILE = "file";
    private final CarService carService;

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findAllCars(ServerRequest serverRequest) {
        return carService.findAllCars()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(carResponses -> ServerResponse.ok().bodyValue(carResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findCarById(ServerRequest serverRequest) {
        return carService.findCarById(serverRequest.pathVariable(ID))
                .flatMap(carResponse -> ServerResponse.ok().bodyValue(carResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findCarsByMakeInsensitiveCase(ServerRequest serverRequest) {
        return carService.findCarsByMakeInsensitiveCase(serverRequest.pathVariable(MAKE))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(carResponses -> ServerResponse.ok().bodyValue(carResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findCarsByFilterInsensitiveCase(ServerRequest serverRequest) {
        return carService.findCarsByFilterInsensitiveCase(serverRequest.pathVariable(FILTER))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(carResponses -> ServerResponse.ok().bodyValue(carResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> getAvailableCar(ServerRequest serverRequest) {
        return carService.getAvailableCar(serverRequest.pathVariable(ID))
                .flatMap(availableCarInfo -> ServerResponse.ok().bodyValue(availableCarInfo))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> getAllAvailableCars(ServerRequest serverRequest) {
        return carService.getAllAvailableCars()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(carResponses -> ServerResponse.ok().bodyValue(carResponses));
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> getCarImage(ServerRequest serverRequest) {
        return carService.getCarImage(serverRequest.pathVariable(ID))
                .flatMap(carResponse -> ServerResponse.ok().bodyValue(carResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> countCars(ServerRequest serverRequest) {
        return carService.countCars()
                .flatMap(numberOfCars -> ServerResponse.ok().bodyValue(numberOfCars));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> saveCar(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .map(carRequestMultivalueMap -> carService.saveCar(carRequestMultivalueMap.toSingleValueMap()))
                .flatMap(carResponse -> ServerResponse.ok().body(carResponse, CarResponse.class));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> uploadCars(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .map(multiValueMap -> multiValueMap.get(FILE))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .concatMap(carService::uploadCars)
                .collectList()
                .flatMap(carResponses -> ServerResponse.ok().bodyValue(carResponses));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> updateCar(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .flatMap(updatedCarRequestMultivalueMap -> carService.updateCar(
                                serverRequest.pathVariable(ID),
                                updatedCarRequestMultivalueMap.toSingleValueMap()
                        )
                )
                .flatMap(carResponse -> ServerResponse.ok().bodyValue(carResponse));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> deleteCarById(ServerRequest serverRequest) {
        return carService.deleteCarById(serverRequest.pathVariable(ID))
                .then(ServerResponse.noContent().build());
    }

}
