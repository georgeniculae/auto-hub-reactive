package com.autohubreactive.agency.router;

import com.autohubreactive.agency.handler.BranchHandler;
import com.autohubreactive.agency.swaggeroperation.SwaggerBranchRouterOperations;
import com.autohubreactive.agency.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BranchRouter {

    @Bean
    @SwaggerBranchRouterOperations
    public RouterFunction<ServerResponse> routeBranch(BranchHandler branchHandler) {
        return RouterFunctions.nest(RequestPredicates.path(Constants.BRANCHES_REQUEST_MAPPING).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                RouterFunctions.route(RequestPredicates.GET(""), branchHandler::findAllBranches)
                        .andRoute(RequestPredicates.GET("/filter/{filter}"), branchHandler::findBranchesByFilterInsensitiveCase)
                        .andRoute(RequestPredicates.GET("/count"), branchHandler::countBranches)
                        .andRoute(RequestPredicates.GET("/{id}"), branchHandler::findBranchById)
                        .andRoute(RequestPredicates.POST(""), branchHandler::saveBranch)
                        .andRoute(RequestPredicates.PUT("/{id}"), branchHandler::updateBranch)
                        .andRoute(RequestPredicates.DELETE("/{id}"), branchHandler::deleteBranchById));
    }

}
