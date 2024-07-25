package com.nvc.ca_house.dto.request;

import lombok.Data;

@Data
public class UserCreationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
