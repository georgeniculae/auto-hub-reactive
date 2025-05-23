package com.autohubreactive.expense.producer;

import com.autohubreactive.dto.common.CarUpdateDetails;
import com.autohubreactive.expense.util.TestUtil;
import com.autohubreactive.lib.retry.RetryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MimeType;
import reactor.test.StepVerifier;
import reactor.util.retry.RetrySpec;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarStatusUpdateProducerServiceTest {

    @InjectMocks
    private CarStatusUpdateProducerService carStatusUpdateProducerService;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private RetryHandler retryHandler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(carStatusUpdateProducerService, "binderName", "carStatusUpdateProducer");
        ReflectionTestUtils.setField(carStatusUpdateProducerService, "mimeType", "application/json");
    }

    @Test
    void sendCarUpdateDetailsTest_success() {
        CarUpdateDetails carUpdateDetails =
                TestUtil.getResourceAsJson("/data/CarUpdateDetails.json", CarUpdateDetails.class);

        when(streamBridge.send(anyString(), any(Object.class), any(MimeType.class))).thenReturn(true);
        when(retryHandler.retry()).thenReturn(RetrySpec.backoff(0, Duration.ofMinutes(0)));

        carStatusUpdateProducerService.sendCarUpdateDetails(carUpdateDetails)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
