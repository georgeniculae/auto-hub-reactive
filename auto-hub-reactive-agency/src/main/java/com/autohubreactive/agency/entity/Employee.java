package com.autohubreactive.agency.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employee")
public record Employee(
    @BsonProperty("_id")
    @BsonId
    ObjectId id,

    @NonNull
    String firstName,

    @NonNull
    String lastName,

    @NonNull
    String jobPosition,

    Branch workingBranch
) {
}
