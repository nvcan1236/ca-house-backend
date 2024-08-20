package com.nvc.motel_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationRequest {
    String city;
    String district;
    String ward;
    String street;
    String other;
    Double longitude;
    Double latitude;
}
