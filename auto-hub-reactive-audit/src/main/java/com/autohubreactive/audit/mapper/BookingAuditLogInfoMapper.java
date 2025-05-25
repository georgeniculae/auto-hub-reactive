package com.autohubreactive.audit.mapper;

import com.autohubreactive.audit.entity.BookingAuditLogInfo;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface BookingAuditLogInfoMapper {

    AuditLogInfoRequest mapEntityToDto(BookingAuditLogInfo bookingAuditLogInfo);

    BookingAuditLogInfo mapDtoToEntity(AuditLogInfoRequest auditLogInfoRequest);

}
