package com.nvc.motel_service.service;

import com.nvc.event.dto.NotificationEvent;
import com.nvc.event.enums.TemplateEnum;
import com.nvc.motel_service.dto.request.AppointmentRequest;
import com.nvc.motel_service.dto.response.AppointmentResponse;
import com.nvc.motel_service.dto.response.UserResponse;
import com.nvc.motel_service.entity.Appointment;
import com.nvc.motel_service.enums.AppointmentStatus;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.AppointmentMapper;
import com.nvc.motel_service.repository.AppointmentRepository;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    AppointmentMapper appointmentMapper;
    MotelRepository motelRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    UserClient userClient;

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

        UserResponse userResponse = userClient.getUserById(appointment.getUserId()).getResult();

        Map<String, String> params = new HashMap<>();
        params.put("viewerName", userResponse.getLastName() + " " + userResponse.getFirstName());
        params.put("viewerEmail", userResponse.getEmail());
        params.put("roomTitle", appointment.getMotel().getName());
        params.put("roomAddress", appointment.getMotel().getLocation().getFullLocation());
        params.put("viewingTime", appointment.getDate().toString());
        params.put("roomLink", "https://cahouse.vn/motel/" + appointment.getMotel().getId());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(userResponse.getEmail())
                .template(TemplateEnum.APPOINTMENT)
                .params(params)
                .build();

        kafkaTemplate.send("notification-delivery", notificationEvent);
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

    public List<AppointmentResponse> getByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentRepository.findAllByUserId(username)
                .stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
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
