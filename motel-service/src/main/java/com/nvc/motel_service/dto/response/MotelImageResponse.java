package com.nvc.motel_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotelImageResponse {
    String id;
    String url;
}
