package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {
    String id;
    String userId;
    Instant createdAt;
    AppointmentStatus status;
    Instant date;
}
