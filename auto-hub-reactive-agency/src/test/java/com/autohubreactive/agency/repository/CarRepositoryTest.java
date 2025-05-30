package com.autohubreactive.agency.repository;

import com.autohubreactive.agency.entity.Car;
import com.autohubreactive.agency.util.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

import static com.mongodb.assertions.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DataMongoTest
class CarRepositoryTest {

    private static final Car CAR_1 = TestUtil.getResourceAsJson("/data/Car1.json", Car.class);
    private static final Car CAR_2 = TestUtil.getResourceAsJson("/data/Car2.json", Car.class);

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void initCollection() {
        carRepository.deleteAll()
                .thenMany(carRepository.saveAll(List.of(CAR_1, CAR_2)))
                .blockLast();
    }

    @Test
    void checkIfConnectionEstablished() {
        assertTrue(mongoDbContainer.isCreated());
    }

    @Test
    void findByIdTest_success() {
        carRepository.findById(new ObjectId("64f361caf291ae086e179549"))
                .as(StepVerifier::create)
                .assertNext(actualCar -> assertThat(actualCar).usingRecursiveComparison().isEqualTo(CAR_1))
                .verifyComplete();
    }

    @Test
    void findAllByFilterInsensitiveCaseTest_success() {
        carRepository.findAllByFilterInsensitiveCase("Volkswagen")
                .as(StepVerifier::create)
                .assertNext(actualCar -> assertThat(actualCar).usingRecursiveComparison().isEqualTo(CAR_2))
                .verifyComplete();
    }

    @Test
    void findImageByCarIdTest_success() {
        carRepository.findImageByCarId(new ObjectId("64f361caf291ae086e179549"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findCarsByMakeInsensitiveCaseTest_success() {
        carRepository.findCarsByMakeInsensitiveCase("Audi")
                .as(StepVerifier::create)
                .assertNext(actualCar -> assertThat(actualCar).usingRecursiveComparison().isEqualTo(CAR_1))
                .verifyComplete();
    }

    @Test
    void findAllCarsTest_success() {
        carRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findAllAvailableCarsTest_success() {
        carRepository.findAllAvailableCars()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

}
