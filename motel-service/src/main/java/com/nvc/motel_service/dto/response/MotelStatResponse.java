package com.nvc.motel_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotelStatResponse {
    List<StatTypeResponse> byType;
    List<StatPriceResponse> byPrice;
    List<StatAreaResponse> byArea;
    List<StatPeriodResponse> byPeriod;
}
