package com.autohubreactive.agency.mapper;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.agency.util.AssertionUtil;
import com.autohubreactive.agency.util.TestUtil;
import com.autohubreactive.dto.agency.RentalOfficeRequest;
import com.autohubreactive.dto.agency.RentalOfficeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class RentalOfficeMapperTest {

    private final RentalOfficeMapper rentalOfficeMapper = new RentalOfficeMapperImpl();

    @Test
    void mapEntityToDtoTest_success() {
        RentalOffice rentalOffice =
                TestUtil.getResourceAsJson("/data/RentalOffice2.json", RentalOffice.class);

        RentalOfficeResponse rentalOfficeDto = rentalOfficeMapper.mapEntityToDto(rentalOffice);

        assertNotNull(rentalOfficeDto);
        AssertionUtil.assertRentalOfficeResponse(rentalOffice, rentalOfficeDto);
    }

    @Test
    void mapEntityToDtoTest_null() {
        assertNull(rentalOfficeMapper.mapEntityToDto(null));
    }

    @Test
    void getNewRentalOfficeTest_success() {
        Branch branch = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        RentalOfficeRequest rentalOfficeDto =
                TestUtil.getResourceAsJson("/data/RentalOfficeRequest.json", RentalOfficeRequest.class);

        RentalOffice newRentalOffice = rentalOfficeMapper.getNewRentalOffice(rentalOfficeDto, branch);

        assertNotNull(newRentalOffice);
        AssertionUtil.assertRentalOfficeRequest(newRentalOffice, rentalOfficeDto);
    }

    @Test
    void getNewRentalOfficeTest_null() {
        assertNull(rentalOfficeMapper.getNewRentalOffice(null, null));
    }

}
