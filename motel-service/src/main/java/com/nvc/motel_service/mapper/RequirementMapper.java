package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.RequirementResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Requirement;
import com.nvc.motel_service.enums.Job;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RequirementMapper {

    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.listToString(request.getJobs()))")
    Requirement toRequirement(RequirementRequest request);


    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.listToString(request.getJobs()))")
    void updateRequirement(@MappingTarget Requirement requirement, RequirementRequest request);

    @Mapping(target = "jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.stringToList(requirement.getJobs()))")
    RequirementResponse toRequirementResponse(Requirement requirement);
}