package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.request.ReviewRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.PriceService;
import com.nvc.motel_service.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/{motelId}/review")
    public ApiResponse create(@RequestBody ReviewRequest request,
                              @PathVariable String motelId) {
        reviewService.create(motelId, request);
        return ApiResponse.builder()
                .message("Thêm review thành công")
                .build();
    }

    @GetMapping("/{motelId}/review")
    public ApiResponse findAll(@PathVariable String motelId) {
        return ApiResponse.builder()
                .result(reviewService.findAll(motelId))
                .build();
    }

    @PutMapping("/review/{reviewId}")
    public ApiResponse update(@PathVariable String reviewId,
                              @RequestBody ReviewRequest request) {
        reviewService.update(reviewId, request);
        return ApiResponse.builder()
                .message("Cập nhật review thành công")
                .build();
    }

    @DeleteMapping("/review/{reviewId}")
    public ApiResponse delete(@PathVariable String reviewId) {
        reviewService.delete(reviewId);
        return ApiResponse.builder()
                .message("Xoá review thành công")
                .build();
    }


}
