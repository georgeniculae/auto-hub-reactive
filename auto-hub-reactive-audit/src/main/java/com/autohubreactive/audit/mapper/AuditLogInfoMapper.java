package com.autohubreactive.audit.mapper;

import com.autohubreactive.audit.entity.AuditLogInfo;
import com.autohubreactive.dto.common.AuditLogInfoRequest;
import com.autohubreactive.dto.common.ParameterInfo;
import com.autohubreactive.exception.AutoHubException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AuditLogInfoMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mapping(target = "parametersInfo", expression = "java(convertToString(auditLogInfoRequest.parameters()))")
    AuditLogInfo mapDtoToEntity(AuditLogInfoRequest auditLogInfoRequest);

    default String convertToString(List<ParameterInfo> parameterInfos) {
        try {
            return OBJECT_MAPPER.writeValueAsString(parameterInfos);
        } catch (JsonProcessingException e) {
            throw new AutoHubException(e.getMessage());
        }
    }

}
