package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.LocationRequest;
import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.service.LocationService;
import com.nvc.motel_service.service.MotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LocationController {
    LocationService locationService;

    @PostMapping("/{motelId}/location")
    public ApiResponse create(@RequestBody LocationRequest request,
                              @PathVariable String motelId) {
        locationService.create(motelId, request);
        return ApiResponse.builder()
                .message("Thêm vị trí thành công")
                .build();
    }

    @PutMapping("/location/{locationId}")
    public ApiResponse update(@PathVariable String locationId,
                              @RequestBody LocationRequest request) {
        locationService.update(locationId, request);
        return ApiResponse.builder()
                .message("Cập nhật vị trí thành công")
                .build();
    }


}
