package com.autohubreactive.agency.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "car")
@Builder(toBuilder = true)
public record Car(
    @BsonProperty("_id")
    @BsonId
    ObjectId id,

    String make,

    String model,

    BodyType bodyType,

    Integer yearOfProduction,

    String color,

    Integer mileage,

    @With
    CarStatus carStatus,

    BigDecimal amount,

    @With
    Branch originalBranch,

    @With
    Branch actualBranch
) {
}
