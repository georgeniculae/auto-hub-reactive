package com.autohubreactive.expense.producer;

import com.autohubreactive.dto.common.CarUpdateDetails;
import com.autohubreactive.lib.retry.RetryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarStatusUpdateProducerService {

    private final StreamBridge streamBridge;
    private final RetryHandler retryHandler;

    @Value("${spring.cloud.stream.bindings.carUpdateDetailsProducer-out-0.destination}")
    private String binderName;

    @Value("${spring.cloud.stream.bindings.carUpdateDetailsProducer-out-0.contentType}")
    private String mimeType;

    public Mono<Void> sendCarUpdateDetails(CarUpdateDetails carUpdateDetails) {
        return Mono.fromRunnable(
                        () -> streamBridge.send(
                                binderName,
                                buildMessage(carUpdateDetails),
                                MimeType.valueOf(mimeType)
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .retryWhen(retryHandler.retry())
                .then();
    }

    private Message<CarUpdateDetails> buildMessage(CarUpdateDetails carUpdateDetails) {
        return MessageBuilder.withPayload(carUpdateDetails)
                .build();
    }

}
