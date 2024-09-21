package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.AppointmentRequest;
import com.nvc.motel_service.entity.Appointment;
import com.nvc.motel_service.enums.AppointmentStatus;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.repository.AppointmentRepository;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    MotelRepository motelRepository;

    public void create(String motelId, AppointmentRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Appointment appointment = Appointment.builder()
                .createdAt(Instant.now())
                .date(request.getDate())
                .status(AppointmentStatus.PENDING)
                .motel(motelRepository.findById(motelId)
                        .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND)))
                .userId(username)
                .build();
        appointmentRepository.save(appointment);
    }

    public void update(String id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        appointment.setDate(request.getDate());
        appointmentRepository.save(appointment);
    }

    public void changeStatus(String id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentRepository.findAllByUserId(username);
    }
    public List<Appointment> getByMotelOwner() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentRepository.findAllByMotel_OwnerId(username);
    }

    public void delete(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        appointmentRepository.delete(appointment);
    }


}
