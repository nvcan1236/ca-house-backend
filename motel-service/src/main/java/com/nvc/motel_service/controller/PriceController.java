package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.LocationRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.LocationService;
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
public class PriceController {
    PriceService priceService;

    @PostMapping("/{motelId}/price")
    public ApiResponse create(@RequestBody PriceRequest request,
                              @PathVariable String motelId) {
        priceService.create(motelId, request);
        return ApiResponse.builder()
                .message("Thêm giá thành công")
                .build();
    }

    @PutMapping("/price/{priceId}")
    public ApiResponse update(@PathVariable String priceId,
                              @RequestBody PriceRequest request) {
        priceService.update(priceId, request);
        return ApiResponse.builder()
                .message("Cập nhật giá thành công")
                .build();
    }

    @DeleteMapping("/price/{priceId}")
    public ApiResponse delete(@PathVariable String priceId) {
        priceService.delete(priceId);
        return ApiResponse.builder()
                .message("Xoá giá thành công")
                .build();
    }


}
