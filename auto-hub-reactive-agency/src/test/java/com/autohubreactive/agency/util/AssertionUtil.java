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
        assertEquals(car.getMake(), carRequest.make());
        assertEquals(car.getModel(), carRequest.model());
        assertEquals(car.getBodyType().name(), carRequest.bodyCategory().name());
        assertEquals(car.getYearOfProduction(), carRequest.yearOfProduction());
        assertEquals(car.getColor(), carRequest.color());
        assertEquals(car.getMileage(), carRequest.mileage());
        assertEquals(car.getCarStatus().name(), carRequest.carState().name());
        assertEquals(car.getAmount(), carRequest.amount());
    }

    public static void assertCarResponse(Car car, CarResponse carResponse) {
        assertEquals(car.getMake(), carResponse.make());
        assertEquals(car.getModel(), carResponse.model());
        assertEquals(car.getBodyType().name(), carResponse.bodyCategory().name());
        assertEquals(car.getYearOfProduction(), carResponse.yearOfProduction());
        assertEquals(car.getColor(), carResponse.color());
        assertEquals(car.getMileage(), carResponse.mileage());
        assertEquals(car.getCarStatus().name(), carResponse.carState().name());
        assertEquals(car.getAmount(), carResponse.amount());
    }

    public static void assertBranchRequest(Branch branch, BranchRequest branchRequest) {
        assertEquals(branch.getName(), branchRequest.name());
        assertEquals(branch.getAddress(), branchRequest.address());
    }

    public static void assertBranchResponse(Branch branch, BranchResponse branchResponse) {
        assertEquals(branch.getName(), branchResponse.name());
        assertEquals(branch.getAddress(), branchResponse.address());
    }

    public static void assertRentalOfficeRequest(RentalOffice rentalOffice, RentalOfficeRequest rentalOfficeRequest) {
        assertEquals(rentalOffice.getName(), rentalOfficeRequest.name());
        assertEquals(rentalOffice.getContactAddress(), rentalOfficeRequest.contactAddress());
        assertEquals(rentalOffice.getPhoneNumber(), rentalOfficeRequest.phoneNumber());
    }

    public static void assertRentalOfficeResponse(RentalOffice rentalOffice, RentalOfficeResponse rentalOfficeResponse) {
        assertEquals(rentalOffice.getName(), rentalOfficeResponse.name());
        assertEquals(rentalOffice.getContactAddress(), rentalOfficeResponse.contactAddress());
        assertEquals(rentalOffice.getPhoneNumber(), rentalOfficeResponse.phoneNumber());
    }

    public static void assertEmployeeRequest(Employee employee, EmployeeRequest employeeRequest) {
        assertEquals(employee.getFirstName(), employeeRequest.firstName());
        assertEquals(employee.getLastName(), employeeRequest.lastName());
        assertEquals(employee.getJobPosition(), employeeRequest.jobPosition());
        assertEquals(employee.getFirstName(), employeeRequest.firstName());
    }

    public static void assertEmployeeResponse(Employee employee, EmployeeResponse employeeResponse) {
        assertEquals(employee.getFirstName(), employeeResponse.firstName());
        assertEquals(employee.getLastName(), employeeResponse.lastName());
        assertEquals(employee.getJobPosition(), employeeResponse.jobPosition());
        assertEquals(employee.getFirstName(), employeeResponse.firstName());
    }

}
