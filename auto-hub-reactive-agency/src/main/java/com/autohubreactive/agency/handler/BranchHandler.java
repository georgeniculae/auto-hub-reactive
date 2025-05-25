package com.autohubreactive.agency.handler;

import com.autohubreactive.agency.service.BranchService;
import com.autohubreactive.agency.util.Constants;
import com.autohubreactive.agency.validator.BranchRequestValidator;
import com.autohubreactive.dto.agency.BranchRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchService branchService;
    private final BranchRequestValidator branchRequestValidator;

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> findAllBranches(ServerRequest serverRequest) {
        return branchService.findAllBranches()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(branchResponses -> ServerResponse.ok().bodyValue(branchResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findBranchById(ServerRequest serverRequest) {
        return branchService.findBranchById(serverRequest.pathVariable(Constants.ID))
                .flatMap(branchResponse -> ServerResponse.ok().bodyValue(branchResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> findBranchesByFilterInsensitiveCase(ServerRequest serverRequest) {
        return branchService.findBranchesByFilterInsensitiveCase(serverRequest.pathVariable(Constants.FILTER))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(branchResponses -> ServerResponse.ok().bodyValue(branchResponses))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("hasRole('user')")
    public Mono<ServerResponse> countBranches(ServerRequest serverRequest) {
        return branchService.countBranches()
                .flatMap(numberOfBranches -> ServerResponse.ok().bodyValue(numberOfBranches));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> saveBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequest.class)
                .flatMap(branchRequestValidator::validateBody)
                .flatMap(branchService::saveBranch)
                .flatMap(branchResponse -> ServerResponse.ok().bodyValue(branchResponse));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> updateBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequest.class)
                .flatMap(branchRequestValidator::validateBody)
                .flatMap(branchRequest -> branchService.updateBranch(serverRequest.pathVariable(Constants.ID), branchRequest))
                .flatMap(branchResponse -> ServerResponse.ok().bodyValue(branchResponse));
    }

    @PreAuthorize("hasRole('admin')")
    public Mono<ServerResponse> deleteBranchById(ServerRequest serverRequest) {
        return branchService.deleteBranchById(serverRequest.pathVariable(Constants.ID))
                .then(ServerResponse.noContent().build());
    }

}
