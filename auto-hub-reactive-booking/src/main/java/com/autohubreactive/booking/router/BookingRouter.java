package com.autohubreactive.booking.router;

import com.autohubreactive.booking.handler.BookingHandler;
import com.autohubreactive.booking.swaggeroperation.SwaggerBookingRouterOperations;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookingRouter {

    @Bean
    @SwaggerBookingRouterOperations
    public RouterFunction<ServerResponse> routeBooking(BookingHandler bookingHandler) {
        return RouterFunctions.nest(
                RequestPredicates.path(StringUtils.EMPTY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                RouterFunctions.route(RequestPredicates.GET("/list"), bookingHandler::findAllBookings)
                        .andRoute(RequestPredicates.GET("/date/{date}"), bookingHandler::findBookingsByDateOfBooking)
                        .andRoute(RequestPredicates.GET("/count"), bookingHandler::countBookings)
                        .andRoute(RequestPredicates.GET("/count-by-logged-in-user"), bookingHandler::countBookingsOfLoggedInUser)
                        .andRoute(RequestPredicates.GET("/current-date"), bookingHandler::getCurrentDate)
                        .andRoute(RequestPredicates.GET("/by-current-user"), bookingHandler::findBookingsByLoggedInUser)
                        .andRoute(RequestPredicates.GET("/{id}"), bookingHandler::findBookingById)
                        .andRoute(RequestPredicates.POST("/new"), bookingHandler::saveBooking)
                        .andRoute(RequestPredicates.PUT("/{id}"), bookingHandler::updateBooking)
        );
    }

}
