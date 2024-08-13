package com.nvc.user_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailUserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String avatar;

    Set<RoleResponse> roles;
    ProfileResponse profile;
}
