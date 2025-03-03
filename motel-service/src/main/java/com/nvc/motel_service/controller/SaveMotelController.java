package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.service.SavingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SaveMotelController {
    SavingService savingService;
    @PostMapping("save/{motelId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse saveMotel(@PathVariable String motelId) {
        savingService.saveMotel(motelId);
        return ApiResponse.builder().message("Đã lưu trọ thành công").build();
    }

    @GetMapping("/saved")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<MotelResponse>> getSavedMotelByUser() {
        List<MotelResponse> saved =  savingService.getSavedMotels();
        return ApiResponse.<List<MotelResponse>>builder().result(saved).build();
    }
}
