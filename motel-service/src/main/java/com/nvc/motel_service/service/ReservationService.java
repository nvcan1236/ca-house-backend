package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.response.PageResponse;
import com.nvc.motel_service.dto.response.ReservationResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Reservation;
import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.ReservationStatus;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.ReservationRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class ReservationService {
    ReservationRepository reservationRepository;
    MotelRepository motelRepository;

    public PageResponse<ReservationResponse> getReservationByUser(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var reservationsData = reservationRepository.findAllByCreatedBy(pageable, username);
        return PageResponse.<ReservationResponse>builder()
                .currentPage(page)
                .pageSize(reservationsData.getSize())
                .totalPage(reservationsData.getTotalPages())
                .totalElement(reservationsData.getTotalElements())
                .data(reservationsData.stream().map(
                        entity -> ReservationResponse.builder()
                                .id(entity.getId())
                                .amount(entity.getAmount())
                                .motelId(entity.getMotel().getId())
                                .createdBy(entity.getCreatedBy())
                                .createdAt(entity.getCreatedAt())
                                .status(entity.getStatus())
                                .build()).toList())
                .build();
    }

    public String create(int amount, String motelId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Motel motel = motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Reservation reservation = Reservation.builder()
                .amount(amount)
                .createdBy(username)
                .createdAt(Instant.now())
                .motel(motel)
                .status(ReservationStatus.PENDING)
                .build();
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    public void updateStatus(String reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        reservation.setStatus(status);
        Motel motel = reservation.getMotel();
        motel.setStatus(MotelStatus.RESERVED);
        motelRepository.save(motel);
        reservationRepository.save(reservation);
    }

}
