package com.autohubreactive.agency.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "car")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Car {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;
    private String make;
    private String model;
    private BodyType bodyType;
    private Integer yearOfProduction;
    private String color;
    private Integer mileage;
    private CarStatus carStatus;
    private BigDecimal amount;
    private Branch originalBranch;
    private Branch actualBranch;

}
