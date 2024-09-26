package com.nvc.motel_service.controller;

import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.dto.response.ReservationCreationResponse;
import com.nvc.motel_service.dto.response.ReservationResponse;
import com.nvc.motel_service.enums.ReservationStatus;
import com.nvc.motel_service.service.ReservationService;
import com.nvc.motel_service.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
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
public class ReservationController {

    ReservationService reservationService;
    VNPayService vnPayService;

    @GetMapping("/reserve/user")
    public ApiResponse<PageResponse<ReservationResponse>> getReservationByUser(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReservationResponse>>builder()
                .result(reservationService.getReservationByUser(page, size))
                .build();
    }

    @GetMapping("/reserve/{motelId}/payment/vn-pay")
    public ApiResponse<ReservationCreationResponse> pay(HttpServletRequest request,
                                                                      @RequestParam int amount,
                                                                      @PathVariable String motelId) {
        String reservationId = reservationService.create(amount, motelId);
        return ApiResponse.<ReservationCreationResponse>builder()
                .result(ReservationCreationResponse.builder()
                        .paymentUrl(vnPayService.createVnPayPayment(request, amount, reservationId))
                        .reservationId(reservationId)
                        .build())
                .build();
    }

    @PutMapping("/reserve/{reservationId}/status")
    public ApiResponse updateStatus(@PathVariable String reservationId,
                                    @RequestParam ReservationStatus status) {
        reservationService.updateStatus(reservationId, status);
        return ApiResponse.builder()
                .message("Update Status thành công")
                .build();
    }
}
