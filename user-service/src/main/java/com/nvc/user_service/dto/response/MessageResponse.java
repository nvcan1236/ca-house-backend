package com.nvc.user_service.dto.response;

import com.google.cloud.Timestamp;
import com.nvc.user_service.enums.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    List<String> content;
    MessageType type;
    String createdBy;
    Timestamp createdAt;
    String roomId;
}
