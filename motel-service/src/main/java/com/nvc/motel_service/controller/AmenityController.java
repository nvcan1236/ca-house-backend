package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.AmenityRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.AmenityService;
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
public class AmenityController {
    AmenityService amenityService;

    @PostMapping("/{motelId}/amenity")
    public ApiResponse create(@RequestBody AmenityRequest request,
                              @PathVariable String motelId) {
        amenityService.create(motelId, request);
        return ApiResponse.builder()
                .message("Thêm tiện nghi thành công")
                .build();
    }

    @PutMapping("/amenity/{locationId}")
    public ApiResponse update(@PathVariable String locationId,
                              @RequestBody AmenityRequest request) {
        amenityService.update(locationId, request);
        return ApiResponse.builder()
                .message("Cập nhật tiện nghi thành công")
                .build();
    }

    @DeleteMapping("/amenity/{locationId}")
    public ApiResponse delete(@PathVariable String locationId) {
        amenityService.delete(locationId);
        return ApiResponse.builder()
                .message("Xoá tiện nghi thành công")
                .build();
    }


}
