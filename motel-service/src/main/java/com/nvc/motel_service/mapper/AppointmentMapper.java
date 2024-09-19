package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.AppointmentResponse;
import com.nvc.motel_service.dto.response.RequirementResponse;
import com.nvc.motel_service.entity.Appointment;
import com.nvc.motel_service.entity.Requirement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponse toAppointmentResponse(Appointment appointment);
}