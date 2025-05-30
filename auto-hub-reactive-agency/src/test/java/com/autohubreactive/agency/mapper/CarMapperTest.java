package com.autohubreactive.agency.mapper;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.Car;
import com.autohubreactive.agency.util.AssertionUtil;
import com.autohubreactive.agency.util.TestUtil;
import com.autohubreactive.dto.agency.CarRequest;
import com.autohubreactive.dto.agency.CarResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CarMapperTest {

    private final CarMapper carMapper = new CarMapperImpl();

    @Test
    void mapEntityToDtoTest_success() {
        Car car = TestUtil.getResourceAsJson("/data/Car1.json", Car.class);

        CarResponse carResponse = carMapper.mapEntityToDto(car);

        assertNotNull(carResponse);
        AssertionUtil.assertCarResponse(car, carResponse);
    }

    @Test
    void mapEntityToDtoTest_null() {
        assertNull(carMapper.mapEntityToDto(null));
    }

    @Test
    void getNewCarTest_success() {
        CarRequest carRequest = TestUtil.getResourceAsJson("/data/CarRequest.json", CarRequest.class);
        Branch originalBranch = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);
        Branch actualBranch = TestUtil.getResourceAsJson("/data/Branch1.json", Branch.class);

        Car car = carMapper.getNewCar(carRequest, originalBranch, actualBranch);

        assertNotNull(car);
        AssertionUtil.assertCarRequest(car, carRequest);
    }

    @Test
    void getNewCarTest_null() {
        assertNull(carMapper.getNewCar(null, null, null));
    }

}
