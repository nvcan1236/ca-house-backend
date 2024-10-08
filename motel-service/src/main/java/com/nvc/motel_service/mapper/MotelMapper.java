package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.DetailMotelResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Motel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MotelMapper {
    Motel toMotel (MotelCreationRequest request);
    @Mapping(target = "district", source = "location.district")
    @Mapping(target = "city", source = "location.city")
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "isApproved", source = "approved")
    MotelResponse toMotelResponse(Motel motel);

    @Mapping(target = "requirement.jobs", expression = "java(com.nvc.motel_service.enums.JobConverter.stringToList(requirement.getJobs()))")
    @Mapping(target = "isApproved", source = "approved")
    DetailMotelResponse toDetailMotelResponse(Motel motel);

    void updateMotel(@MappingTarget Motel motel, MotelUpdationRequest request);
}
