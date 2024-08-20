package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.AmenityRequest;
import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.AmenityResponse;
import com.nvc.motel_service.dto.response.DetailMotelResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.Motel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    Amenity toAmenity (AmenityRequest request);
    AmenityResponse toAmenityResponse (Amenity amenity);
    void updateAmenity(@MappingTarget Amenity amenity, AmenityRequest request);
}
