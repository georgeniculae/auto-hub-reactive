package com.autohubreactive.audit.repository;

import com.autohubreactive.audit.entity.AuditLogInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuditLogInfoRepository extends ReactiveMongoRepository<AuditLogInfo, ObjectId> {
}
