package com.nvc.ca_house.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    Set<String> roles;
}
