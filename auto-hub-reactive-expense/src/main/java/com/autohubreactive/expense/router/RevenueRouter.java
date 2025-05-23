package com.autohubreactive.expense.router;

import com.autohubreactive.expense.handler.RevenueHandler;
import com.autohubreactive.expense.swaggeroperation.SwaggerRouteRevenueOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RevenueRouter {

    private static final String REQUEST_MAPPING = "/revenues";

    @Bean
    @SwaggerRouteRevenueOperation
    public RouterFunction<ServerResponse> routeRevenue(RevenueHandler revenueHandler) {
        return RouterFunctions.nest(RequestPredicates.path(REQUEST_MAPPING).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                RouterFunctions.route(RequestPredicates.GET(""), revenueHandler::findAllRevenues)
                        .andRoute(RequestPredicates.GET("/total"), revenueHandler::getTotalAmount)
                        .andRoute(RequestPredicates.GET("/{date}"), revenueHandler::findRevenuesByDate));
    }

}
