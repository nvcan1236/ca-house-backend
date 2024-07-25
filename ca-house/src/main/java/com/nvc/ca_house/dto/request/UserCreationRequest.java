package com.nvc.ca_house.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreationRequest {
    private String username;
    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
