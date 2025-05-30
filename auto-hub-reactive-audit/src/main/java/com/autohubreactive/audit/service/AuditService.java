package com.autohubreactive.audit.service;

import com.autohubreactive.audit.mapper.AuditLogInfoMapper;
import com.autohubreactive.audit.repository.AuditLogInfoRepository;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import com.autohubreactive.exception.AutoHubException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogInfoRepository auditLogInfoRepository;
    private final AuditLogInfoMapper auditLogInfoMapper;

    public Mono<Void> saveAuditLogInfo(AuditLogInfoRequest auditLogInfoRequest) {
        return Mono.just(auditLogInfoMapper.mapDtoToEntity(auditLogInfoRequest))
                .flatMap(auditLogInfoRepository::save)
                .then()
                .onErrorMap(e -> {
                    log.error("Error while saving booking audit log: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

}
