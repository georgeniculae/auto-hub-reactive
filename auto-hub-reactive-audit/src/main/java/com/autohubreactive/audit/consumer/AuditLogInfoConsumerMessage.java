package com.autohubreactive.audit.consumer;

import com.autohubreactive.audit.service.AuditService;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import com.autohubreactive.lib.util.KafkaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuditLogInfoConsumerMessage {

    private final AuditService auditService;

    @Bean
    public Function<Flux<Message<AuditLogInfoRequest>>, Mono<Void>> auditLogInfoConsumer() {
        return messageFlux -> messageFlux.concatMap(this::processMessage)
                .then();
    }

    private Mono<Void> processMessage(Message<AuditLogInfoRequest> message) {
        return auditService.saveAuditLogInfo(message.getPayload())
                .doOnSuccess(_ -> {
                    KafkaUtil.acknowledgeMessage(message.getHeaders());
                    log.info("Booking audit log saved: {}", message.getPayload());
                })
                .onErrorResume(e -> {
                    log.error("Exception during processing saved audit log message: {}", e.getMessage(), e);

                    return Mono.empty();
                });
    }

}
