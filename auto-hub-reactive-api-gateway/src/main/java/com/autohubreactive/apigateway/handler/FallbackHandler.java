package com.autohubreactive.apigateway.handler;

import com.autohubreactive.apigateway.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FallbackHandler {

    public Mono<ServerResponse> fallback(ServerRequest serverRequest) {
        return Mono.just(Constants.MESSAGE)
                .flatMap(message -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(message));
    }

}
