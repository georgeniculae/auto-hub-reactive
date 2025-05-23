package com.autohubreactive.booking.producer.bookingprocessing;

import com.autohubreactive.lib.retry.RetryHandler;
import lombok.RequiredArgsConstructor;
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
public class DeletedBookingProducerService {

    private final StreamBridge streamBridge;
    private final RetryHandler retryHandler;

    @Value("${spring.cloud.stream.bindings.deletedBookingProducer-out-0.destination}")
    private String binderName;

    @Value("${spring.cloud.stream.bindings.deletedBookingProducer-out-0.contentType}")
    private String mimeType;

    public Mono<Void> sendMessage(String bookingId) {
        return Mono.fromRunnable(
                        () -> streamBridge.send(
                                binderName,
                                buildMessage(bookingId),
                                MimeType.valueOf(mimeType)
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .retryWhen(retryHandler.retry())
                .then();
    }

    private Message<String> buildMessage(String bookingId) {
        return MessageBuilder.withPayload(bookingId)
                .build();
    }

}
