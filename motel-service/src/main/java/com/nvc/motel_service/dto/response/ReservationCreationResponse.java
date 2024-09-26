package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.ReservationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationCreationResponse {
    String paymentUrl;
    String reservationId;
}
