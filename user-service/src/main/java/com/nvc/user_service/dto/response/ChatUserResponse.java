package com.nvc.user_service.dto.response;

import com.nvc.user_service.enums.MessageType;
import com.nvc.user_service.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatUserResponse {
    List<UserRole> role;
    String displayName;
    String avatar;
}
