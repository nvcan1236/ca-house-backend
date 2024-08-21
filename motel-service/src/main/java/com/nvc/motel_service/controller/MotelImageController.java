package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.MotelImageRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.service.MotelImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MotelImageController {
    MotelImageService motelImageService;


    @PostMapping(value = "/{motelId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse getAll(@PathVariable String motelId,
                              @RequestParam("images") List<MultipartFile> files) {
        motelImageService.create(motelId, files);
        return ApiResponse.builder()
                .message("Thêm hình ảnh thành công.")
                .build();
    }

    @DeleteMapping("/images/images/{imageId}")
    public ApiResponse delete(@PathVariable String imageId) {
        motelImageService.delete(imageId);
        return ApiResponse.builder()
                .message("Xoá ảnh thành công")
                .build();
    }
}
