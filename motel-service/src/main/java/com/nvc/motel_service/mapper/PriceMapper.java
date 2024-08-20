package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.PriceResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Price;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    Price toPrice (PriceRequest request);
    PriceResponse toPriceResponse(Price price);
    void updatePrice(@MappingTarget Price price, PriceRequest request);
}
