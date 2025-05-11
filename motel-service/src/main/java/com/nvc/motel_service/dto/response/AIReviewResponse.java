package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.AIReviewStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIReviewResponse {
    AIReviewStatus status;
    String reason;
}
