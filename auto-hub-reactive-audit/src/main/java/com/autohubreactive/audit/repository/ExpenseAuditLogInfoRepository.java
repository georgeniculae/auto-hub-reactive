package com.autohubreactive.audit.repository;

import com.autohubreactive.audit.entity.ExpenseAuditLogInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ExpenseAuditLogInfoRepository extends ReactiveMongoRepository<ExpenseAuditLogInfo, ObjectId> {
}
