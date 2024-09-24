package com.nvc.user_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatByPeriodReponse {
    String period;
    long userCount;
    long ownerCount;
}
