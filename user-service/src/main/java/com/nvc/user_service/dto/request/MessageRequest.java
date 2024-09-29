package com.nvc.user_service.dto.request;

import com.nvc.user_service.enums.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
    List<String> content;
    MessageType type;
    String recipient;
}
