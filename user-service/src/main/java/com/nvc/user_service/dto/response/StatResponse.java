package com.nvc.user_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatResponse {
    List<StatByPeriodReponse> byPeriod;
    List<StatByRoleReponse> byRole;
}
