package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.PriceService;
import com.nvc.motel_service.service.RequirementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RequirementController {
    RequirementService requirementService;

    @PostMapping("/{motelId}/requirement")
    public ApiResponse create(@RequestBody RequirementRequest request,
                              @PathVariable String motelId) {
        requirementService.create(motelId, request);
        return ApiResponse.builder()
                .message("Thêm yêu cầu thành công")
                .build();
    }

    @PutMapping("/requirement/{requirementId}")
    public ApiResponse update(@PathVariable String requirementId,
                              @RequestBody RequirementRequest request) {
        requirementService.update(requirementId, request);
        return ApiResponse.builder()
                .message("Cập nhật yêu cầu thành công")
                .build();
    }

    @DeleteMapping("/requirement/{requirementId}")
    public ApiResponse delete(@PathVariable String requirementId) {
        requirementService.delete(requirementId);
        return ApiResponse.builder()
                .message("Xoá yêu cầu thành công")
                .build();
    }


}
