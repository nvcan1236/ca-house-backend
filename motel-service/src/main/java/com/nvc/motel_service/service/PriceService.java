package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Price;
import com.nvc.motel_service.entity.Requirement;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.PriceMapper;
import com.nvc.motel_service.mapper.RequirementMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.PriceRepository;
import com.nvc.motel_service.repository.RequirementRepository;
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
public class PriceService {

    PriceRepository priceRepository;
    PriceMapper priceMapper;
    MotelRepository motelRepository;

    public void create(String motelId, PriceRequest request) {
        Price price = priceMapper.toPrice(request);
        price.setMotel(motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND)));
        priceRepository.save(price);
    }

    public void update(String id, PriceRequest request) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        priceMapper.updatePrice(price, request);
        priceRepository.save(price);
    }

    public void delete(String id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        priceRepository.delete(price);
    }


}
