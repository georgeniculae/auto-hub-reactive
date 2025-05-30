package com.autohubreactive.agency.migration;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.Car;
import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.agency.util.SerializerUtil;

import java.util.List;

public class DatabaseCollectionCreator {

    public static List<RentalOffice> getRentalOffices() {
        return List.of(
                SerializerUtil.getResourceAsJson("/migration/rental_office/BucurestiRentalOffice.json", RentalOffice.class),
                SerializerUtil.getResourceAsJson("/migration/rental_office/PloiestiRentalOffice.json", RentalOffice.class)
        );
    }

    public static List<Branch> getBranches() {
        return List.of(
                SerializerUtil.getResourceAsJson("/migration/branch/BucurestiBranch.json", Branch.class),
                SerializerUtil.getResourceAsJson("/migration/branch/PloiestiBranch.json", Branch.class)
        );
    }

    public static List<Car> getCars() {
        return List.of(
                SerializerUtil.getResourceAsJson("/migration/car/AudiA4.json", Car.class),
                SerializerUtil.getResourceAsJson("/migration/car/VolkswagenGolf.json", Car.class)
        );
    }

    public static List<Employee> getEmployees() {
        return List.of(
                SerializerUtil.getResourceAsJson("/migration/employee/AndreiIonescu.json", Employee.class),
                SerializerUtil.getResourceAsJson("/migration/employee/AlexandruOprescu.json", Employee.class),
                SerializerUtil.getResourceAsJson("/migration/employee/IonPopescu.json", Employee.class),
                SerializerUtil.getResourceAsJson("/migration/employee/MariusAlexandrescu.json", Employee.class)
        );
    }

}
