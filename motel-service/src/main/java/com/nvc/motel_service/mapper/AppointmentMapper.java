package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.response.AppointmentResponse;
import com.nvc.motel_service.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponse toAppointmentResponse(Appointment appointment);
}