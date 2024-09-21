package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.DetailMotelResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.dto.response.UserResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.enums.AmenityType;
import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.MotelMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class MotelService {
    MotelRepository motelRepository;
    MotelMapper motelMapper;
    UserClient userClient;
    GeometryFactory geometryFactory;
    DateTimeFormatter dateTimeFormatter;

    public PageResponse<MotelResponse> getAll(int page, int size,
                                              MotelType roomType,
                                              Double minPrice, Double maxPrice,
                                              List<String> amenities) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        long amenitiesSize = (amenities != null) ? amenities.size() : 0;
        var motelData = motelRepository.findAllFiltered(pageable, roomType
                , minPrice, maxPrice
                , amenities, amenitiesSize);

        return PageResponse.<MotelResponse>builder()
                .currentPage(page)
                .pageSize(motelData.getSize())
                .totalPage(motelData.getTotalPages())
                .totalElement(motelData.getTotalElements())
                .data(motelData.stream().map(motelMapper::toMotelResponse).toList())
                .build();
    }

    public DetailMotelResponse getMotelById(String id) {
        Motel motel = motelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        DetailMotelResponse detailMotelResponse = motelMapper.toDetailMotelResponse(motel);
        detailMotelResponse.setCreatedAt(dateTimeFormatter.format(motel.getCreatedAt()));
        return detailMotelResponse;
    }

    public MotelResponse create(MotelCreationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Motel motel = motelMapper.toMotel(request);
        motel.setCreatedAt(Instant.now());
        motel.setStatus(MotelStatus.AVAILABLE);
        motel.setOwnerId(username);
        motelRepository.save(motel);
        return motelMapper.toMotelResponse(motel);
    }

    public MotelResponse update(String motelId, MotelUpdationRequest request) {
        Motel motel = motelRepository.findById(motelId).
                orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        try {
            motelMapper.updateMotel(motel, request);
            return motelMapper.toMotelResponse(motel);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public List<MotelResponse> getNearestMotel(Double longitude, Double latitude, Double radius) {
        Point point = geometryFactory.createPoint(
                new Coordinate(longitude, latitude)
        );
        return motelRepository.findNearestMotels(point, radius)
                .stream().map(motelMapper::toMotelResponse).toList();
    }

    public List<MotelResponse> getMotelsByUser(String userId) {
        return motelRepository.findByOwnerId(userId).stream().map(motelMapper::toMotelResponse).toList();
    }
}
