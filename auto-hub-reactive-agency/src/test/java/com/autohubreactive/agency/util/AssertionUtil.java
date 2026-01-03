package com.autohubreactive.agency.util;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.Car;
import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.dto.agency.BranchRequest;
import com.autohubreactive.dto.agency.BranchResponse;
import com.autohubreactive.dto.agency.CarRequest;
import com.autohubreactive.dto.agency.CarResponse;
import com.autohubreactive.dto.agency.EmployeeRequest;
import com.autohubreactive.dto.agency.EmployeeResponse;
import com.autohubreactive.dto.agency.RentalOfficeRequest;
import com.autohubreactive.dto.agency.RentalOfficeResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionUtil {

    public static void assertCarRequest(Car car, CarRequest carRequest) {
        assertEquals(car.make(), carRequest.make());
        assertEquals(car.model(), carRequest.model());
        assertEquals(car.bodyType().name(), carRequest.bodyCategory().name());
        assertEquals(car.yearOfProduction(), carRequest.yearOfProduction());
        assertEquals(car.color(), carRequest.color());
        assertEquals(car.mileage(), carRequest.mileage());
        assertEquals(car.carStatus().name(), carRequest.carState().name());
        assertEquals(car.amount(), carRequest.amount());
    }

    public static void assertCarResponse(Car car, CarResponse carResponse) {
        assertEquals(car.make(), carResponse.make());
        assertEquals(car.model(), carResponse.model());
        assertEquals(car.bodyType().name(), carResponse.bodyCategory().name());
        assertEquals(car.yearOfProduction(), carResponse.yearOfProduction());
        assertEquals(car.color(), carResponse.color());
        assertEquals(car.mileage(), carResponse.mileage());
        assertEquals(car.carStatus().name(), carResponse.carState().name());
        assertEquals(car.amount(), carResponse.amount());
    }

    public static void assertBranchRequest(Branch branch, BranchRequest branchRequest) {
        assertEquals(branch.name(), branchRequest.name());
        assertEquals(branch.address(), branchRequest.address());
    }

    public static void assertBranchResponse(Branch branch, BranchResponse branchResponse) {
        assertEquals(branch.name(), branchResponse.name());
        assertEquals(branch.address(), branchResponse.address());
    }

    public static void assertRentalOfficeRequest(RentalOffice rentalOffice, RentalOfficeRequest rentalOfficeRequest) {
        assertEquals(rentalOffice.name(), rentalOfficeRequest.name());
        assertEquals(rentalOffice.contactAddress(), rentalOfficeRequest.contactAddress());
        assertEquals(rentalOffice.phoneNumber(), rentalOfficeRequest.phoneNumber());
    }

    public static void assertRentalOfficeResponse(RentalOffice rentalOffice, RentalOfficeResponse rentalOfficeResponse) {
        assertEquals(rentalOffice.name(), rentalOfficeResponse.name());
        assertEquals(rentalOffice.contactAddress(), rentalOfficeResponse.contactAddress());
        assertEquals(rentalOffice.phoneNumber(), rentalOfficeResponse.phoneNumber());
    }

    public static void assertEmployeeRequest(Employee employee, EmployeeRequest employeeRequest) {
        assertEquals(employee.firstName(), employeeRequest.firstName());
        assertEquals(employee.lastName(), employeeRequest.lastName());
        assertEquals(employee.jobPosition(), employeeRequest.jobPosition());
        assertEquals(employee.firstName(), employeeRequest.firstName());
    }

    public static void assertEmployeeResponse(Employee employee, EmployeeResponse employeeResponse) {
        assertEquals(employee.firstName(), employeeResponse.firstName());
        assertEquals(employee.lastName(), employeeResponse.lastName());
        assertEquals(employee.jobPosition(), employeeResponse.jobPosition());
        assertEquals(employee.firstName(), employeeResponse.firstName());
    }

}
