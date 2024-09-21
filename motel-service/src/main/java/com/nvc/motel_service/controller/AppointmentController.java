package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.AppointmentRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.AppointmentResponse;
import com.nvc.motel_service.enums.AppointmentStatus;
import com.nvc.motel_service.mapper.AppointmentMapper;
import com.nvc.motel_service.service.AppointmentService;
import com.nvc.motel_service.service.PriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentController {
    private final AppointmentMapper appointmentMapper;
    AppointmentService appointmentService;

    @GetMapping("/appointment/user")
    public ApiResponse<List<AppointmentResponse>> getByUser() {
        List<AppointmentResponse> appointmentResponses = appointmentService.getByUser()
                .stream().map(appointmentMapper::toAppointmentResponse)
                .toList();

        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentResponses)
                .build();
    }

    @GetMapping("/appointment/owner")
    public ApiResponse<List<AppointmentResponse>> getByMotelOwner() {
        List<AppointmentResponse> appointmentResponses = appointmentService.getByMotelOwner()
                .stream().map(appointmentMapper::toAppointmentResponse)
                .toList();

        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentResponses)
                .build();
    }

    @PostMapping("/{motelId}/appointment")
    public ApiResponse create(@PathVariable String motelId,
                              @RequestBody AppointmentRequest request) {
        appointmentService.create(motelId, request);
        return ApiResponse.builder()
                .message("Đặt ngày xem phòng thành công")
                .build();
    }

    @PutMapping("/appointment/{appointmentId}")
    public ApiResponse update(@PathVariable String appointmentId,
                              @RequestBody AppointmentRequest request) {
        appointmentService.update(appointmentId, request);
        return ApiResponse.builder()
                .message("Cập nhật ngày xem phòng thành công")
                .build();
    }

    @PutMapping("/appointment/{appointmentId}/update-status")
    public ApiResponse updateStatus(@PathVariable String appointmentId,
                              @RequestParam String status) {
        appointmentService.changeStatus(appointmentId, AppointmentStatus.valueOf(status));
        return ApiResponse.builder()
                .message("Cập nhật ngày xem phòng thành công")
                .build();
    }

    @DeleteMapping("/appointment/{appointmentId}")
    public ApiResponse delete(@PathVariable String appointmentId) {
        appointmentService.delete(appointmentId);
        return ApiResponse.builder()
                .message("Huỷ ngày xem phòng thành công")
                .build();
    }


}
