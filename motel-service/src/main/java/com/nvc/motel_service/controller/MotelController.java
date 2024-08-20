package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.DetailMotelResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.service.MotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MotelController {
    MotelService motelService;

    @GetMapping("/")
    public ApiResponse<PageResponse<MotelResponse>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return ApiResponse.<PageResponse<MotelResponse>>builder()
                .result(motelService.getAll(page, size))
                .build();
    }

    @GetMapping("{motelId}")
    public ApiResponse<DetailMotelResponse> gatById(@PathVariable String motelId) {
        return ApiResponse.<DetailMotelResponse>builder()
                .result(motelService.getMotelById(motelId))
                .build();
    }

    @PostMapping("/")
    public ApiResponse<MotelResponse> create(@RequestBody MotelCreationRequest request) {
        return ApiResponse.<MotelResponse>builder()
                .result(motelService.create(request))
                .build();
    }

    @PutMapping("/{motelId}")
    public ApiResponse<MotelResponse> update(@PathVariable String motelId,
                                             @RequestBody MotelUpdationRequest request) {
        return ApiResponse.<MotelResponse>builder()
                .result(motelService.update(motelId, request))
                .build();
    }
}
