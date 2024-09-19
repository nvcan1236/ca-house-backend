package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.request.AppointmentRequest;
import com.nvc.motel_service.dto.request.PriceRequest;
import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.AppointmentResponse;
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

    @GetMapping("/appointment/{userId}")
    public ApiResponse<List<AppointmentResponse>> getByUserId(@PathVariable String userId) {
        List<AppointmentResponse> appointmentResponses = appointmentService.getByUserId(userId)
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

    @DeleteMapping("/appointment/{appointmentId}")
    public ApiResponse delete(@PathVariable String appointmentId) {
        appointmentService.delete(appointmentId);
        return ApiResponse.builder()
                .message("Huỷ ngày xem phòng thành công")
                .build();
    }


}
