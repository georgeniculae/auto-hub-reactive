package com.autohubreactive.agency.mapper;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.agency.entity.RentalOffice;
import com.autohubreactive.dto.agency.RentalOfficeRequest;
import com.autohubreactive.dto.agency.RentalOfficeResponse;
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
public interface RentalOfficeMapper {

    @Mapping(target = "branchId", expression = "java(mapObjectIdToString(rentalOffice.branch().id()))")
    RentalOfficeResponse mapEntityToDto(RentalOffice rentalOffice);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", expression = "java(branch)")
    @Mapping(target = "name", expression = "java(rentalOfficeRequest.name())")
    @Mapping(target = "city", expression = "java(rentalOfficeRequest.city())")
    @Mapping(target = "address", expression = "java(rentalOfficeRequest.address())")
    RentalOffice getNewRentalOffice(RentalOfficeRequest rentalOfficeRequest, Branch branch);

    @Mapping(target = "id", expression = "java(existingRentalOfficeId)")
    @Mapping(target = "name", expression = "java(updatedRentalOfficeRequest.name())")
    @Mapping(target = "city", expression = "java(updatedRentalOfficeRequest.city())")
    @Mapping(target = "address", expression = "java(updatedRentalOfficeRequest.address())")
    @Mapping(target = "branch", expression = "java(branch)")
    RentalOffice getUpdatedRentalOffice(
            ObjectId existingRentalOfficeId,
            RentalOfficeRequest updatedRentalOfficeRequest,
            Branch branch
    );

    default String mapObjectIdToString(ObjectId id) {
        return ObjectUtils.isEmpty(id) ? null : id.toString();
    }

}
