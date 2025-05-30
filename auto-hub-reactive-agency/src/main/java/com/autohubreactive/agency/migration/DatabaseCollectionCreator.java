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
                JsonUtil.getResourceAsJson("/migration/rental_office/BucurestiRentalOffice.json", RentalOffice.class),
                JsonUtil.getResourceAsJson("/migration/rental_office/PloiestiRentalOffice.json", RentalOffice.class)
        );
    }

    public static List<Branch> getBranches() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/branch/BucurestiBranch.json", Branch.class),
                JsonUtil.getResourceAsJson("/migration/branch/PloiestiBranch.json", Branch.class)
        );
    }

    public static List<Car> getCars() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/car/AudiA4.json", Car.class),
                JsonUtil.getResourceAsJson("/migration/car/VolkswagenGolf.json", Car.class)
        );
    }

    public static List<Employee> getEmployees() {
        return List.of(
                JsonUtil.getResourceAsJson("/migration/employee/AndreiIonescu.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/AlexandruOprescu.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/IonPopescu.json", Employee.class),
                JsonUtil.getResourceAsJson("/migration/employee/MariusAlexandrescu.json", Employee.class)
        );
    }

}
