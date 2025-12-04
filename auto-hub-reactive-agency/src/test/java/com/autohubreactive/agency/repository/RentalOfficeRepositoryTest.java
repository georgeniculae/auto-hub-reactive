package com.autohubreactive.agency.repository;

import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.agency.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import reactor.test.StepVerifier;

import java.util.List;

import static com.mongodb.assertions.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DataMongoTest
class RentalOfficeRepositoryTest {

    private static final RentalOffice RENTAL_OFFICE_1 = TestUtil.getResourceAsJson("/data/RentalOffice2.json", RentalOffice.class);
    private static final RentalOffice RENTAL_OFFICE_2 = TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private RentalOfficeRepository rentalOfficeRepository;

    @BeforeEach
    void initCollection() {
        rentalOfficeRepository.deleteAll()
                .thenMany(rentalOfficeRepository.saveAll(List.of(RENTAL_OFFICE_1, RENTAL_OFFICE_2)))
                .blockLast();
    }

    @Test
    void checkIfConnectionEstablished() {
        assertTrue(mongoDbContainer.isCreated());
    }

    @Test
    void findAllByFilterInsensitiveCaseTest_success() {
        rentalOfficeRepository.findAllByFilterInsensitiveCase("Rental Office")
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

}
