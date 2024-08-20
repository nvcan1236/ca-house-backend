package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.PriceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequirementResponse {
    String id;
    String name;
    String description;
}
