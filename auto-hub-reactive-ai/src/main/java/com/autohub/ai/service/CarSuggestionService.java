package com.autohub.ai.service;

import com.autohub.ai.util.Constants;
import com.autohubreactive.dto.agency.CarResponse;
import com.autohubreactive.dto.ai.CarSuggestionResponse;
import com.autohubreactive.dto.ai.TripInfo;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarSuggestionService {

    private final ChatService chatService;
    private final CarService carService;

    public Mono<CarSuggestionResponse> getChatOutput(String apikey, List<String> roles, TripInfo tripInfo) {
        return getAvailableCars(apikey, roles)
                .collectList()
                .flatMap(cars -> getCarSuggestionResponse(tripInfo, cars))
                .onErrorMap(e -> {
                    log.error("Error while getting chat response: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    private Mono<CarSuggestionResponse> getCarSuggestionResponse(TripInfo tripInfo, List<String> cars) {
        return chatService.getChatReply(getText(), getParams(tripInfo, cars));
    }

    private Flux<String> getAvailableCars(String apikey, List<String> roles) {
        return carService.getAllAvailableCars(apikey, roles)
                .map(this::getCarDetails);
    }

    private String getCarDetails(CarResponse carResponse) {
        return carResponse.make() + " " + carResponse.model() + " from " + carResponse.yearOfProduction();
    }

    private String getText() {
        return """
                Which car from the following list {cars} is more suitable for rental from a rental car
                agency for a trip for {peopleCount} people to {destination}, Romania in {month}?
                The car will be used for {tripKind}.""";
    }

    private Map<String, Object> getParams(TripInfo tripInfo, List<String> cars) {
        return Map.of(
                Constants.CARS, cars,
                Constants.DESTINATION, tripInfo.destination(),
                Constants.PEOPLE_COUNT, tripInfo.peopleCount(),
                Constants.MONTH, getMonth(tripInfo.tripDate()),
                Constants.TRIP_KIND, tripInfo.tripKind()
        );
    }

    private String getMonth(LocalDate tripDate) {
        return tripDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}
