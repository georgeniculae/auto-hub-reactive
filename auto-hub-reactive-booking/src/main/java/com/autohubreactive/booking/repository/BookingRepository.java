package com.autohubreactive.booking.repository;

import com.autohubreactive.booking.entity.Booking;
import com.autohubreactive.booking.entity.BookingStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingRepository extends ReactiveMongoRepository<Booking, ObjectId> {

    @Query("""
            { 'customerUsername': ?0 }""")
    Flux<Booking> findByCustomerUsername(String customerUsername);

    Mono<Boolean> existsByCustomerUsernameAndStatus(String customerUsername, BookingStatus status);

    @Query(
            value = """
                    { 'customerUsername': ?0 }""",
            count = true
    )
    Mono<Long> countByCustomerUsername(String customerUsername);

}
