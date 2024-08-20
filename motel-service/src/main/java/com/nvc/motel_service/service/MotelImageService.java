package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.AmenityRequest;
import com.nvc.motel_service.dto.request.MotelImageRequest;
import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.MotelImage;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.AmenityMapper;
import com.nvc.motel_service.repository.AmenityRepository;
import com.nvc.motel_service.repository.MotelImageRepository;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class MotelImageService {
    MotelImageRepository motelImageRepository;
    MotelRepository motelRepository;

    public void create(String motelId, MotelImageRequest request) {
        List<MultipartFile> images = request.getImages();


    }

    public void delete(String id) {
        MotelImage image = motelImageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        motelImageRepository.delete(image);
    }

}
