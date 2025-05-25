package com.autohubreactive.audit.mapper;

import com.autohubreactive.audit.entity.CustomerAuditLogInfo;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CustomerAuditLogInfoMapper {

    AuditLogInfoRequest mapEntityToDto(CustomerAuditLogInfo customerAuditLogInfo);

    CustomerAuditLogInfo mapDtoToEntity(AuditLogInfoRequest auditLogInfoRequest);

}
