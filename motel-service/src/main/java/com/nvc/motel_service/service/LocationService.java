package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.LocationRequest;
import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.LocationResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Location;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.LocationMapper;
import com.nvc.motel_service.repository.LocationRepository;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class LocationService {
    LocationRepository locationRepository;
    LocationMapper locationMapper;
    MotelRepository motelRepository;

    public void create(String motelId, LocationRequest request) {
        Location location = locationMapper.tolLocation(request);
        location.setMotel(motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND)));
        locationRepository.save(location);
    }

    public void update(String id,LocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        locationMapper.updateLocation(location, request);
        locationRepository.save(location);
    }

    public void delete(String id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        locationRepository.delete(location);

    }

}
