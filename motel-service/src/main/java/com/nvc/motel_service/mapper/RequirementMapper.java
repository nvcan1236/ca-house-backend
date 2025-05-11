package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.RequirementResponse;
import com.nvc.motel_service.entity.Requirement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RequirementMapper {

    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.listToString(request.getJobs()))")
    Requirement toRequirement(RequirementRequest request);


    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.listToString(request.getJobs()))")
    void updateRequirement(@MappingTarget Requirement requirement, RequirementRequest request);

    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.stringToList(requirement.getJobs()))")
    RequirementResponse toRequirementResponse(Requirement requirement);
}