package com.autohubreactive.agency.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rental_office")
public record RentalOffice(
    @BsonProperty("_id")
    @BsonId
    ObjectId id,

    @NonNull
    String name,

    @NonNull
    String contactAddress,

    String phoneNumber
) {
}
