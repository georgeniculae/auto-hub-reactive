package com.autohubreactive.audit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Document(collection = "audit_log_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuditLogInfo {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;

    @NonNull
    private String methodName;

    private LocalDateTime timestamp;

    private String username;

    private String parametersInfo;

}
