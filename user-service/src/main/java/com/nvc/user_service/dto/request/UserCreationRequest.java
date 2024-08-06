package com.nvc.user_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    String password;
    String firstName;
    String lastName;
    String email;
    Set<String> roles;
}
