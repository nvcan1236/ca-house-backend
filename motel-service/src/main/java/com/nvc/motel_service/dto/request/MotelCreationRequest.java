package com.nvc.motel_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotelCreationRequest {
    String name;
    String description;
    Double price;
    String type;
    Double area;
    Instant availableDate;
}
