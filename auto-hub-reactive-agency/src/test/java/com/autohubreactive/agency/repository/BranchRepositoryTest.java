package com.autohubreactive.agency.repository;

import com.autohubreactive.agency.entity.Branch;
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
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DataMongoTest
class BranchRepositoryTest {

    private static final Branch RENTAL_BRANCH_1 = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);
    private static final Branch RENTAL_BRANCH_2 = TestUtil.getResourceAsJson("/data/Branch2.json", Branch.class);

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private BranchRepository branchRepository;

    @BeforeEach
    void initCollection() {
        branchRepository.deleteAll()
                .thenMany(branchRepository.saveAll(List.of(RENTAL_BRANCH_1, RENTAL_BRANCH_2)))
                .blockLast();
    }

    @Test
    void checkIfConnectionEstablished() {
        assertTrue(mongoDbContainer.isCreated());
    }

    @Test
    void findAllByFilterInsensitiveCaseTest_success() {
        branchRepository.findAllByFilterInsensitiveCase("Branch")
                .as(StepVerifier::create)
                .assertNext(branch -> assertThat(branch).usingRecursiveComparison().isEqualTo(RENTAL_BRANCH_1))
                .assertNext(branch -> assertThat(branch).usingRecursiveComparison().isEqualTo(RENTAL_BRANCH_2))
                .verifyComplete();
    }

}
