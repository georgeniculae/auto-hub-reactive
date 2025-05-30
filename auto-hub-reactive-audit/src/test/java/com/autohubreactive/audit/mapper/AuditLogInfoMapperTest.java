package com.autohubreactive.audit.mapper;

import com.autohubreactive.audit.entity.AuditLogInfo;
import com.autohubreactive.audit.util.AssertionUtil;
import com.autohubreactive.audit.util.TestUtil;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditLogInfoMapperTest {

    private final AuditLogInfoMapper auditLogInfoMapper = new AuditLogInfoMapperImpl();

    @Test
    void mapDtoToEntityTest_success() {
        AuditLogInfoRequest auditLogInfoRequest =
                TestUtil.getResourceAsJson("/data/AuditLogInfoRequest.json", AuditLogInfoRequest.class);

        AuditLogInfo auditLogInfo = auditLogInfoMapper.mapDtoToEntity(auditLogInfoRequest);

        AssertionUtil.assertAuditLogInfo(auditLogInfoRequest, auditLogInfo);
    }

}
