package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.PriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationController {
    PriceService priceService;

    @PostMapping("/{motelId}/reservation")
    public ApiResponse create(@PathVariable String motelId) {

        return ApiResponse.builder()
                .message("Thêm giá thành công")
                .build();
    }

    @PutMapping("/price/{locationId}")
    public ApiResponse update(@PathVariable String locationId,
                              @RequestBody PriceRequest request) {
        priceService.update(locationId, request);
        return ApiResponse.builder()
                .message("Cập nhật giá thành công")
                .build();
    }

    @DeleteMapping("/price/{locationId}")
    public ApiResponse delete(@PathVariable String locationId) {
        priceService.delete(locationId);
        return ApiResponse.builder()
                .message("Cập nhật giá thành công")
                .build();
    }


}
