package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.LocationRequest;
import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Location;
import com.nvc.motel_service.entity.Motel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location tolLocation (LocationRequest request);
    void updateLocation(@MappingTarget Location location, LocationRequest request);
}
