package com.nvc.motel_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    String id;
    String city;
    String district;
    String ward;
    String street;
    String other;
    Double longitude;
    Double latitude;
}
