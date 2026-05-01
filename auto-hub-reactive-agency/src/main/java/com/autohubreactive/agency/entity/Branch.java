package com.autohubreactive.agency.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "branch")
public record Branch(
    @BsonProperty("_id")
    @BsonId
    ObjectId id,

    @NonNull
    String name,

    @NonNull
    String region,

    @NonNull
    String address,

    @NonNull
    String phoneNumber
) {
}
