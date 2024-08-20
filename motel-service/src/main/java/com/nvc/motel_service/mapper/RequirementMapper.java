package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Requirement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RequirementMapper {
    Requirement toRequirement (RequirementRequest request);
    void updateRequirement(@MappingTarget Requirement requirement, RequirementRequest request);
}
