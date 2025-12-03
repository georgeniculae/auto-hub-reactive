package com.autohubreactive.agency.repository;

import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.util.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
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
class EmployeeRepositoryTest {

    private static final Employee EMPLOYEE_1 = TestUtil.getResourceAsJson("/data/Employee1.json", Employee.class);
    private static final Employee EMPLOYEE_2 = TestUtil.getResourceAsJson("/data/Employee2.json", Employee.class);

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void initCollection() {
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.saveAll(List.of(EMPLOYEE_1, EMPLOYEE_2)))
                .blockLast();
    }

    @Test
    void checkIfConnectionEstablished() {
        assertTrue(mongoDbContainer.isCreated());
    }

    @Test
    void findAllByFilterInsensitiveCaseTest_success() {
        employeeRepository.findAllByFilterInsensitiveCase("technician")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findAllEmployeesByBranchIdTest_success() {
        employeeRepository.findAllEmployeesByBranchId(new ObjectId("64f361caf291ae086e179521"))
                .as(StepVerifier::create)
                .assertNext(employee -> assertThat(employee).usingRecursiveComparison().isEqualTo(EMPLOYEE_1))
                .verifyComplete();
    }

    @Test
    void deleteByBranchIdTest_success() {
        employeeRepository.deleteByBranchId(new ObjectId("64f361caf291ae086e179532"))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
