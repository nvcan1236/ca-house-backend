package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.AmenityRequest;
import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.AmenityMapper;
import com.nvc.motel_service.repository.AmenityRepository;
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
public class AmenityService {
    AmenityRepository amenityRepository;
    AmenityMapper amenityMapper;
    MotelRepository motelRepository;


    public void create(String motelId, AmenityRequest request){
        Amenity amenity = amenityMapper.toAmenity(request);
        amenity.setMotel(motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND)));
        amenityRepository.save(amenity);
    }

    public void update(String id,AmenityRequest request) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        amenityMapper.updateAmenity(amenity, request);
        amenityRepository.save(amenity);
    }

    public void delete(String id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        amenityRepository.delete(amenity);
    }

}
