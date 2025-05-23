package com.autohubreactive.customer.service;

import com.autohubreactive.dto.customer.RegisterRequest;
import com.autohubreactive.dto.customer.RegistrationResponse;
import com.autohubreactive.dto.customer.UserInfo;
import com.autohubreactive.dto.customer.UserUpdateRequest;
import com.autohubreactive.exception.AutoHubException;
import com.autohubreactive.lib.aspect.LogActivity;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final KeycloakUserService keycloakUserService;
    private final UsernameProducerService usernameProducerService;

    public Flux<UserInfo> findAllUsers() {
        return Mono.fromCallable(keycloakUserService::findAllUsers)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .onErrorMap(e -> {
                    log.error("Error while getting users: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<UserInfo> findUserByUsername(String username) {
        return Mono.fromCallable(() -> keycloakUserService.findUserByUsername(username))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(e -> {
                    log.error("Error while getting user by username: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<UserInfo> getCurrentUser(String username) {
        return Mono.fromCallable(() -> keycloakUserService.getCurrentUser(username))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(e -> {
                    log.error("Error while getting current user: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<Long> countUsers() {
        return Mono.fromCallable(keycloakUserService::countUsers)
                .subscribeOn(Schedulers.boundedElastic())
                .map(Long::valueOf)
                .onErrorMap(e -> {
                    log.error("Error while counting users: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    @LogActivity(
            sentParameters = "request",
            activityDescription = "User registration"
    )
    public Mono<RegistrationResponse> registerUser(RegisterRequest request) {
        return Mono.fromCallable(() -> keycloakUserService.registerCustomer(request))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(e -> {
                    log.error("Error while registering user: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    @LogActivity(
            sentParameters = "id",
            activityDescription = "User update"
    )
    public Mono<UserInfo> updateUser(String id, UserUpdateRequest userUpdateRequest) {
        return Mono.fromCallable(() -> keycloakUserService.updateUser(id, userUpdateRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(e -> {
                    log.error("Error while updating user: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    @LogActivity(
            sentParameters = "username",
            activityDescription = "User deletion"
    )
    public Mono<Void> deleteUserByUsername(String username) {
        return Mono.fromRunnable(() -> keycloakUserService.deleteUserByUsername(username))
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.defer(() -> usernameProducerService.sendUsername(username)))
                .onErrorMap(e -> {
                    log.error("Error while deleting user: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<Void> signOut(String id) {
        return Mono.fromRunnable(() -> keycloakUserService.signOut(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then()
                .onErrorMap(e -> {
                    log.error("Error while signing out user: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

}
