package com.autohubreactive.agency.mapper;

import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.dto.agency.EmployeeRequest;
import com.autohubreactive.dto.agency.EmployeeResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EmployeeMapper {

    @Mapping(target = "workingRentalOfficeId", expression = "java(mapObjectIdToString(employee.workingRentalOffice().id()))")
    EmployeeResponse mapEntityToDto(Employee employee);

    @Mapping(target = "workingRentalOffice", expression = "java(workingRentalOffice)")
    Employee getNewEmployee(EmployeeRequest employeeRequest, RentalOffice workingRentalOffice);

    @Mapping(target = "id", expression = "java(existingEmployee.id())")
    @Mapping(target = "firstName", expression = "java(updatedEmployeeRequest.firstName())")
    @Mapping(target = "lastName", expression = "java(updatedEmployeeRequest.lastName())")
    @Mapping(target = "jobPosition", expression = "java(updatedEmployeeRequest.jobPosition())")
    @Mapping(target = "workingRentalOffice", source = "workingRentalOffice")
    Employee getUpdatedEmployee(Employee existingEmployee, EmployeeRequest updatedEmployeeRequest, RentalOffice workingRentalOffice);

    default String mapObjectIdToString(ObjectId id) {
        return ObjectUtils.isEmpty(id) ? null : id.toString();
    }

}
