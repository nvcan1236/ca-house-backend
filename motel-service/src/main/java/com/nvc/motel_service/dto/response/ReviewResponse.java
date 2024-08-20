package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.MotelStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String userId;
    Date createdAt;
    String content;
}
