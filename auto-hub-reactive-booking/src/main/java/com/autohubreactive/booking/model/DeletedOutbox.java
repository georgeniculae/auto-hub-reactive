package com.autohubreactive.booking.model;

import com.autohubreactive.booking.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deleted_outbox")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DeletedOutbox {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;

    private Booking content;

}
