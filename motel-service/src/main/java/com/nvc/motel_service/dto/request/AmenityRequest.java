package com.nvc.motel_service.dto.request;

import com.nvc.motel_service.enums.AmenityType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AmenityRequest {
    String name;
    AmenityType type;
}
