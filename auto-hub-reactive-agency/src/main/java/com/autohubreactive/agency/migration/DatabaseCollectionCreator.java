package com.autohubreactive.agency.migration;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.Car;
import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.agency.util.JsonUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DatabaseCollectionCreator {

    public static List<RentalOffice> getRentalOffices() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/rental_office/RentalOffice1.json", RentalOffice.class),
                JsonUtil.getResourceAsJson("/migration/rental_office/RentalOffice2.json", RentalOffice.class)
        );
    }

    public static List<Branch> getBranches() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/branch/Branch1.json", Branch.class),
                JsonUtil.getResourceAsJson("/migration/branch/Branch2.json", Branch.class)
        );
    }

    public static List<Car> getCars() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/car/Car1.json", Car.class),
                JsonUtil.getResourceAsJson("/migration/car/Car2.json", Car.class)
        );
    }

    public static List<Employee> getEmployees() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/employee/Employee2.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/Employee1.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/Employee3.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/Employee4.json", Employee.class)
        );
    }

}
