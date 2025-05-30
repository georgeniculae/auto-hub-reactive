package com.autohubreactive.audit.util;

import com.autohubreactive.audit.entity.AuditLogInfo;
import com.autohubreactive.dto.common.AuditLogInfoRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AssertionUtil {

    public static void assertAuditLogInfo(AuditLogInfoRequest auditLogInfoRequest, AuditLogInfo auditLogInfo) {
        assertEquals(auditLogInfoRequest.methodName(), auditLogInfo.getMethodName());
        assertEquals(auditLogInfoRequest.username(), auditLogInfo.getUsername());
        assertFalse(auditLogInfo.getParametersInfo().isBlank());
    }

}
