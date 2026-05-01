package com.autohubreactive.agency.mapper;

import com.autohubreactive.agency.entity.Branch;
import com.autohubreactive.dto.agency.BranchRequest;
import com.autohubreactive.dto.agency.BranchResponse;
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
public interface BranchMapper {

    BranchResponse mapEntityToDto(Branch branch);

    @Mapping(target = "name", expression = "java(branchRequest.name())")
    @Mapping(target = "address", expression = "java(branchRequest.address())")
    @Mapping(target = "region", expression = "java(branchRequest.region())")
    @Mapping(target = "phoneNumber", expression = "java(branchRequest.phoneNumber())")
    Branch getNewBranch(BranchRequest branchRequest);

    @Mapping(target = "id", expression = "java(existingBranchId)")
    @Mapping(target = "name", expression = "java(branchRequest.name())")
    @Mapping(target = "region", expression = "java(branchRequest.region())")
    @Mapping(target = "address", expression = "java(branchRequest.address())")
    @Mapping(target = "phoneNumber", expression = "java(branchRequest.phoneNumber())")
    Branch getUpdatedBranch(ObjectId existingBranchId, BranchRequest branchRequest);

    default String mapObjectIdToString(ObjectId id) {
        return ObjectUtils.isEmpty(id) ? null : id.toString();
    }

}
