package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.DetailMotelResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.dto.response.UserResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.enums.MotelStatus;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class MotelService {
    MotelRepository motelRepository;
    MotelMapper motelMapper;
    UserClient userClient;

    public PageResponse<MotelResponse> getAll(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var motelData = motelRepository.findAll(pageable);
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
        //        UserResponse userResponse = userClient.getUserById(motel.getOwnerId());
//        detailMotelResponse.setOwner(userResponse);
        return motelMapper.toDetailMotelResponse(motel);
    }

    public MotelResponse create(MotelCreationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Motel motel = motelMapper.toMotel(request);
        motel.setCreatedAt(new Date());
        motel.setStatus(MotelStatus.AVAILABLE);
        motel.setOwnerId(username);
        motelRepository.save(motel);
        return motelMapper.toMotelResponse(motel);
    }

    public MotelResponse update(String motelId,MotelUpdationRequest request) {
        Motel motel = motelRepository.findById(motelId).
                orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        try {
            motelMapper.updateMotel(motel, request);
            return motelMapper.toMotelResponse(motel);
        }
        catch (RuntimeException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

}
