package com.autohubreactive.expense.handler;

import com.autohubreactive.dto.invoice.InvoiceRequest;
import com.autohubreactive.expense.service.InvoiceService;
import com.autohubreactive.expense.util.Constants;
import com.autohubreactive.expense.validator.InvoiceRequestValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

    private final InvoiceService invoiceService;
    private final InvoiceRequestValidator invoiceRequestValidator;

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findAllInvoices(ServerRequest serverRequest) {
        return invoiceService.findAllInvoices()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(invoiceResponses -> ServerResponse.ok().bodyValue(invoiceResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findAllActiveInvoices(ServerRequest serverRequest) {
        return invoiceService.findAllActiveInvoices()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(invoiceResponses -> ServerResponse.ok().bodyValue(invoiceResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findAllInvoicesByCustomerUsername(ServerRequest serverRequest) {
        return invoiceService.findAllInvoicesByCustomerUsername(serverRequest.pathVariable(Constants.CUSTOMER_ID))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(invoiceResponses -> ServerResponse.ok().bodyValue(invoiceResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findInvoiceById(ServerRequest serverRequest) {
        return invoiceService.findInvoiceById(serverRequest.pathVariable(Constants.ID))
                .flatMap(invoiceResponse -> ServerResponse.ok().bodyValue(invoiceResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findInvoicesByComments(ServerRequest serverRequest) {
        return invoiceService.findInvoicesByComments(serverRequest.pathVariable(Constants.COMMENTS))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(invoiceResponses -> ServerResponse.ok().bodyValue(invoiceResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> countInvoices(ServerRequest serverRequest) {
        return invoiceService.countInvoices()
                .flatMap(numberOfInvoices -> ServerResponse.ok().bodyValue(numberOfInvoices))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> countAllActiveInvoices(ServerRequest serverRequest) {
        return invoiceService.countAllActiveInvoices()
                .flatMap(numberOfActiveInvoices -> ServerResponse.ok().bodyValue(numberOfActiveInvoices))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> closeInvoice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(InvoiceRequest.class)
                .flatMap(invoiceRequestValidator::validateBody)
                .flatMap(invoiceRequest -> invoiceService.closeInvoice(serverRequest.pathVariable(Constants.ID), invoiceRequest))
                .flatMap(invoiceResponse -> ServerResponse.accepted().bodyValue(invoiceResponse));
    }

}
