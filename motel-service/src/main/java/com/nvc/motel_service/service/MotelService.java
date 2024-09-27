package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.*;
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
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        log.info(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString());
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().toString()
                .contains("ROLE_ADMIN");
        var motelData = motelRepository.findAllFiltered(pageable
                , isAdmin
                , roomType
                , minPrice, maxPrice
                , amenities,
                amenitiesSize);

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

    public MotelResponse approveMotel(String motelId) {
        Motel motel = motelRepository.findById(motelId).
                orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        motel.setApproved(!motel.isApproved());
        motelRepository.save(motel);
        return motelMapper.toMotelResponse(motel);
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

    public List<StatPriceResponse> getMotelsByPriceGroup() {
        return motelRepository.statByPrices().stream().map(
                r -> StatPriceResponse.builder()
                        .range(Float.parseFloat(r[0].toString()))
                        .count(Integer.parseInt(r[1].toString()))
                        .build()
        ).collect(Collectors.toList());
    }

    public List<StatAreaResponse> getMotelsByAreaGroup() {
        return motelRepository.statByArea().stream().map(
                r -> StatAreaResponse.builder()
                        .range(Float.parseFloat(r[0].toString()))
                        .count(Integer.parseInt(r[1].toString()))
                        .build()
        ).collect(Collectors.toList());
    }

    public List<StatPeriodResponse> getMotelsByTime(LocalDate startDate, LocalDate endDate) {
        Instant startInstant = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = endDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        return motelRepository.statByTime(startInstant, endInstant).stream().map(
                r -> StatPeriodResponse.builder()
                        .period(r[1].toString() + "/" + r[0].toString())
                        .count(Integer.parseInt(r[2].toString()))
                        .build()
        ).collect(Collectors.toList());
    }

    public List<StatTypeResponse> getMotelsByType() {
        return motelRepository.statByType().stream().map(
                r -> StatTypeResponse.builder()
                        .type(r[0].toString())
                        .count(Integer.parseInt(r[1].toString()))
                        .build()
        ).collect(Collectors.toList());
    }
}
