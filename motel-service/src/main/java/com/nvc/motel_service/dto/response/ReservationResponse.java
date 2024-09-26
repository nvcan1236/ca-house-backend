package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.ReservationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    String id;
    String createdBy;
    Instant createdAt;
    ReservationStatus status;
    Double amount;
    String motelId;
}
