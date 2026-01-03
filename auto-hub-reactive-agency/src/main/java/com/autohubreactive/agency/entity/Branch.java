package com.autohubreactive.agency.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "branch")
public record Branch(
    @BsonProperty("_id")
    @BsonId
    ObjectId id,
    String name,
    String address,
    RentalOffice rentalOffice
) {
}
