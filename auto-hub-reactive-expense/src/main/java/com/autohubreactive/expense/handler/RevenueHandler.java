package com.autohubreactive.expense.handler;

import com.autohubreactive.expense.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RevenueHandler {

    private static final String DATE_OF_REVENUE = "dateOfRevenue";
    private final RevenueService revenueService;

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> findAllRevenues(ServerRequest serverRequest) {
        return revenueService.findAllRevenues()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(revenueDtoList -> ServerResponse.ok().bodyValue(revenueDtoList))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> findRevenuesByDate(ServerRequest serverRequest) {
        return revenueService.findRevenuesByDate(serverRequest.pathVariable(DATE_OF_REVENUE))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(revenueDtoList -> ServerResponse.ok().bodyValue(revenueDtoList))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> getTotalAmount(ServerRequest serverRequest) {
        return revenueService.getTotalAmount()
                .flatMap(totalAmount -> ServerResponse.ok().bodyValue(totalAmount));
    }

}
