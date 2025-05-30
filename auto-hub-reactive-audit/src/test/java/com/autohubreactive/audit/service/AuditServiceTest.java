package com.autohubreactive.audit.service;

import com.autohubreactive.audit.entity.AuditLogInfo;
import com.autohubreactive.audit.mapper.AuditLogInfoMapper;
import com.autohubreactive.audit.mapper.AuditLogInfoMapperImpl;
import com.autohubreactive.audit.repository.AuditLogInfoRepository;
import com.autohubreactive.audit.util.TestUtil;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @InjectMocks
    private AuditService auditService;

    @Mock
    private AuditLogInfoRepository auditLogInfoRepository;

    @Spy
    private AuditLogInfoMapper auditLogInfoMapper = new AuditLogInfoMapperImpl();

    @Test
    void saveAuditLogInfoTest_success() {
        AuditLogInfo auditLogInfo =
                TestUtil.getResourceAsJson("/data/AuditLogInfo.json", AuditLogInfo.class);
        AuditLogInfoRequest auditLogInfoRequest =
                TestUtil.getResourceAsJson("/data/AuditLogInfoRequest.json", AuditLogInfoRequest.class);

        when(auditLogInfoRepository.save(any(AuditLogInfo.class))).thenReturn(Mono.just(auditLogInfo));

        auditService.saveAuditLogInfo(auditLogInfoRequest)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

        verify(auditLogInfoMapper).mapDtoToEntity(any(AuditLogInfoRequest.class));
    }

    @Test
    void saveBookingAuditLogInfoTest_errorOnSave() {
        AuditLogInfoRequest auditLogInfoRequest =
                TestUtil.getResourceAsJson("/data/AuditLogInfoRequest.json", AuditLogInfoRequest.class);

        when(auditLogInfoRepository.save(any(AuditLogInfo.class))).thenReturn(Mono.error(new Throwable()));

        auditService.saveAuditLogInfo(auditLogInfoRequest)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

}
