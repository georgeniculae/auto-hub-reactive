package com.autohubreactive.agency.router;

import com.autohubreactive.agency.handler.CarHandler;
import com.autohubreactive.agency.swaggeroperation.SwaggerCarRouterOperations;
import com.autohubreactive.agency.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CarRouter {

    @Bean
    @SwaggerCarRouterOperations
    public RouterFunction<ServerResponse> routeCar(CarHandler carHandler) {
        return RouterFunctions.nest(RequestPredicates.path(Constants.CARS_REQUEST_MAPPING),
                RouterFunctions.route(RequestPredicates.GET(""), carHandler::findAllCars)
                        .andRoute(RequestPredicates.GET("/make/{make}"), carHandler::findCarsByMakeInsensitiveCase)
                        .andRoute(RequestPredicates.GET("/filter/{filter}"), carHandler::findCarsByFilterInsensitiveCase)
                        .andRoute(RequestPredicates.GET("/count"), carHandler::countCars)
                        .andRoute(RequestPredicates.GET("/{id}/availability"), carHandler::getAvailableCar)
                        .andRoute(RequestPredicates.GET("/available"), carHandler::getAllAvailableCars)
                        .andRoute(RequestPredicates.GET("/{id}/image"), carHandler::getCarImage)
                        .andRoute(RequestPredicates.GET("/{id}"), carHandler::findCarById)
                        .andRoute(RequestPredicates.POST(""), carHandler::saveCar)
                        .andRoute(RequestPredicates.POST("/upload"), carHandler::uploadCars)
                        .andRoute(RequestPredicates.PUT("/{id}"), carHandler::updateCar)
                        .andRoute(RequestPredicates.DELETE("/{id}"), carHandler::deleteCarById));
    }

}
