package com.autohubreactive.agency.service;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.agency.mapper.RentalOfficeMapper;
import com.autohubreactive.agency.mapper.RentalOfficeMapperImpl;
import com.autohubreactive.agency.repository.EmployeeRepository;
import com.autohubreactive.agency.repository.RentalOfficeRepository;
import com.autohubreactive.agency.util.TestUtil;
import com.autohubreactive.dto.agency.RentalOfficeRequest;
import com.autohubreactive.dto.agency.RentalOfficeResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalOfficeServiceTest {

    @InjectMocks
    private RentalOfficeService rentalOfficeService;

    @Mock
    private RentalOfficeRepository rentalOfficeRepository;

    @Mock
    private BranchService branchService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private RentalOfficeMapper rentalOfficeMapper = new RentalOfficeMapperImpl();

    @Captor
    private ArgumentCaptor<RentalOffice> argumentCaptor = ArgumentCaptor.forClass(RentalOffice.class);

    @Test
    void findAllRentalOfficesTest_success() {
        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        List<RentalOffice> rentalOffices = List.of(rentalOffice);

        RentalOfficeResponse rentalOfficeResponse =
                TestUtil.getResourceAsJson("/data/RentalOfficeResponse.json", RentalOfficeResponse.class);

        when(rentalOfficeRepository.findAll()).thenReturn(Flux.fromIterable(rentalOffices));

        rentalOfficeService.findAllRentalOffices()
                .as(StepVerifier::create)
                .expectNext(rentalOfficeResponse)
                .verifyComplete();
    }

    @Test
    void findAllRentalOfficesTest_errorOnFindingAllRentalOffices() {
        when(rentalOfficeRepository.findAll()).thenReturn(Flux.error(new Throwable()));

        rentalOfficeService.findAllRentalOffices()
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void findRentalOfficeByIdTest_success() {
        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        RentalOfficeResponse rentalOfficeResponse =
                TestUtil.getResourceAsJson("/data/RentalOfficeResponse.json", RentalOfficeResponse.class);

        when(rentalOfficeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(rentalOffice));

        rentalOfficeService.findRentalOfficeById("64f361caf291ae086e179518")
                .as(StepVerifier::create)
                .expectNext(rentalOfficeResponse)
                .verifyComplete();
    }

    @Test
    void findRentalOfficeByIdTest_errorOnFindingById() {
        when(rentalOfficeRepository.findById(any(ObjectId.class))).thenReturn(Mono.error(new Throwable()));

        rentalOfficeService.findRentalOfficeById("64f361caf291ae086e179518")
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void countRentalOfficesTest_success() {
        when(rentalOfficeRepository.count()).thenReturn(Mono.just(3L));

        rentalOfficeService.countRentalOffices()
                .as(StepVerifier::create)
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void countRentalOfficesTest_errorOnCount() {
        when(rentalOfficeRepository.count()).thenReturn(Mono.error(new Throwable()));

        rentalOfficeService.countRentalOffices()
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void saveRentalOfficeTest_success() {
        Branch branch = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        RentalOfficeRequest rentalOfficeRequest =
                TestUtil.getResourceAsJson("/data/RentalOfficeRequest.json", RentalOfficeRequest.class);

        RentalOfficeResponse rentalOfficeResponse =
                TestUtil.getResourceAsJson("/data/RentalOfficeResponse.json", RentalOfficeResponse.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(rentalOfficeRepository.save(any(RentalOffice.class))).thenReturn(Mono.just(rentalOffice));

        rentalOfficeService.saveRentalOffice(rentalOfficeRequest)
                .as(StepVerifier::create)
                .expectNext(rentalOfficeResponse)
                .verifyComplete();

        verify(rentalOfficeRepository).save(argumentCaptor.capture());
        verify(rentalOfficeMapper).mapEntityToDto(any(RentalOffice.class));
    }

    @Test
    void saveRentalOfficeTest_errorOnSaving() {
        Branch branch = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        RentalOfficeRequest rentalOfficeRequest =
                TestUtil.getResourceAsJson("/data/RentalOfficeRequest.json", RentalOfficeRequest.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(rentalOfficeRepository.save(any(RentalOffice.class))).thenReturn(Mono.error(new Throwable()));

        rentalOfficeService.saveRentalOffice(rentalOfficeRequest)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void updateRentalOfficeTest_success() {
        Branch branch =
                TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        RentalOfficeRequest rentalOfficeRequest =
                TestUtil.getResourceAsJson("/data/RentalOfficeRequest.json", RentalOfficeRequest.class);

        RentalOfficeResponse rentalOfficeResponse =
                TestUtil.getResourceAsJson("/data/RentalOfficeResponse.json", RentalOfficeResponse.class);

        when(rentalOfficeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(rentalOffice));
        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(rentalOfficeRepository.save(any(RentalOffice.class))).thenReturn(Mono.just(rentalOffice));

        rentalOfficeService.updateRentalOffice("64f361caf291ae086e179518", rentalOfficeRequest)
                .as(StepVerifier::create)
                .expectNext(rentalOfficeResponse)
                .verifyComplete();
    }

    @Test
    void updateRentalOfficeTest_errorOnSaving() {
        Branch branch =
                TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        RentalOfficeRequest rentalOfficeRequest =
                TestUtil.getResourceAsJson("/data/RentalOfficeRequest.json", RentalOfficeRequest.class);

        when(rentalOfficeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(rentalOffice));
        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(rentalOfficeRepository.save(any(RentalOffice.class))).thenReturn(Mono.error(new Throwable()));

        rentalOfficeService.updateRentalOffice("64f361caf291ae086e179518", rentalOfficeRequest)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void findRentalOfficeByNameTest_success() {
        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice1.json", RentalOffice.class);

        RentalOfficeResponse rentalOfficeResponse =
                TestUtil.getResourceAsJson("/data/RentalOfficeResponse.json", RentalOfficeResponse.class);

        when(rentalOfficeRepository.findAllByFilterInsensitiveCase(anyString())).thenReturn(Flux.just(rentalOffice));

        rentalOfficeService.findRentalOfficesByFilterInsensitiveCase("name")
                .as(StepVerifier::create)
                .expectNext(rentalOfficeResponse)
                .verifyComplete();
    }

    @Test
    void findRentalOfficeByNameTest_errorOnFindingByName() {
        when(rentalOfficeRepository.findAllByFilterInsensitiveCase(anyString())).thenReturn(Flux.error(new Throwable()));

        rentalOfficeService.findRentalOfficesByFilterInsensitiveCase("name")
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    void deleteRentalOfficeByIdTest_success() {
        when(rentalOfficeRepository.deleteById(any(ObjectId.class))).thenReturn(Mono.empty());
        when(employeeRepository.deleteByBranchId(any(ObjectId.class))).thenReturn(Mono.empty());

        rentalOfficeService.deleteRentalOfficeById("64f361caf291ae086e179518")
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteRentalOfficeByIdTest_errorOnDeletingById() {
        when(rentalOfficeRepository.deleteById(any(ObjectId.class))).thenReturn(Mono.error(new Throwable()));

        rentalOfficeService.deleteRentalOfficeById("64f361caf291ae086e179518")
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

}
