package com.nvc.chat_service.dto;


import com.nvc.chat_service.entity.ChatMessage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    String id;
    List<UserResponse> members;
    Instant createdAt;
    ChatMessage lastMessage;
}
