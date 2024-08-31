package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.PriceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceResponse {
    String id;
    PriceType type;
    String name;
    Double value;
    String unit;
}
