package com.nvc.motel_service.dto.request;

import com.nvc.motel_service.enums.PriceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceRequest {
    String type;
    String name;
    Double value;
    String unit;
}
