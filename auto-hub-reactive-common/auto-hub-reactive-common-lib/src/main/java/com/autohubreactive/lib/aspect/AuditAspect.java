package com.autohubreactive.lib.aspect;

import com.autohubreactive.dto.common.AuditLogInfoRequest;
import com.autohubreactive.dto.common.ParameterInfo;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import com.autohubreactive.lib.service.AuditLogProducerService;
import com.autohubreactive.lib.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "audit", name = "enabled")
@Slf4j
public class AuditAspect {

    private final AuditLogProducerService auditLogProducerService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.autohubreactive.lib.aspect.LogActivity)")
    public Mono<?> logActivity(ProceedingJoinPoint joinPoint) {
        return getJoinPointProceed(joinPoint)
                .delayUntil(_ -> sendAuditLogInfoRequest(joinPoint));
    }

    private Mono<?> getJoinPointProceed(ProceedingJoinPoint joinPoint) {
        try {
            return (Mono<?>) joinPoint.proceed();
        } catch (Throwable e) {
            throw ExceptionUtil.handleException(e);
        }
    }

    private Mono<AuditLogInfoRequest> sendAuditLogInfoRequest(ProceedingJoinPoint joinPoint) {
        return extractUsernameHeaderFromWebFluxContext()
                .flatMap(username -> sendAuditLogInfoRequest(joinPoint, username))
                .onErrorMap(ExceptionUtil::handleException);
    }

    private Mono<String> extractUsernameHeaderFromWebFluxContext() {
        return Mono.deferContextual(contextView -> {
            ServerWebExchange exchange = contextView.get(ServerWebExchange.class);

            return extractUsernameHeaderFromRequest(exchange);
        });
    }

    private Mono<String> extractUsernameHeaderFromRequest(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest())
                .map(this::getUsernameHeader);
    }

    private String getUsernameHeader(ServerHttpRequest serverHttpRequest) {
        return Optional.ofNullable(serverHttpRequest.getHeaders().getFirst(Constants.X_USERNAME))
                .orElse(StringUtils.EMPTY);
    }

    private Mono<AuditLogInfoRequest> sendAuditLogInfoRequest(ProceedingJoinPoint joinPoint, String username) {
        AuditLogInfoRequest auditLogInfoRequest = getAuditLogInfoRequest(joinPoint, username);

        return auditLogProducerService.sendAuditLog(auditLogInfoRequest)
                .thenReturn(auditLogInfoRequest);
    }

    private AuditLogInfoRequest getAuditLogInfoRequest(ProceedingJoinPoint joinPoint, String username) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogActivity logActivity = method.getAnnotation(LogActivity.class);

        log.info("Method called: {}", signature);

        List<ParameterInfo> parameters = getParameters(joinPoint, logActivity, signature);
        String methodName = method.getName();
        String activityDescription = logActivity.activityDescription();

        return AuditLogInfoRequest.builder()
                .methodName(methodName)
                .activityDescription(activityDescription)
                .username(username)
                .timestamp(LocalDateTime.now())
                .parameters(parameters)
                .build();
    }

    private List<ParameterInfo> getParameters(
            ProceedingJoinPoint joinPoint,
            LogActivity logActivity,
            MethodSignature signature
    ) {
        return Optional.ofNullable(logActivity)
                .stream()
                .flatMap(activity -> Stream.of(activity.sentParameters()))
                .map(parameter -> extractParameterInfo(joinPoint, signature, parameter))
                .filter(ObjectUtils::isNotEmpty)
                .toList();
    }

    private ParameterInfo extractParameterInfo(ProceedingJoinPoint joinPoint, MethodSignature signature, String parameter) {
        List<String> parameters = Arrays.asList(signature.getParameterNames());
        int indexOfElement = parameters.indexOf(parameter);

        if (indexOfElement < 0) {
            return null;
        }

        Object value = joinPoint.getArgs()[indexOfElement];
        String json = getJson(value);

        return createParameterInfo(parameter, json);
    }

    private String getJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ParameterInfo createParameterInfo(String parameter, String value) {
        return ParameterInfo.builder()
                .parameterName(parameter)
                .parameterValue(value)
                .build();
    }

}
