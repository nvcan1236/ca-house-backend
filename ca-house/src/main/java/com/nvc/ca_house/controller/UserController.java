package com.nvc.ca_house.controller;

import com.nvc.ca_house.dto.request.ApiResponse;
import com.nvc.ca_house.dto.request.UserCreationRequest;
import com.nvc.ca_house.entity.User;
import com.nvc.ca_house.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<User>> getUserList() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<List<User>>();
        apiResponse.setResult(userService.getUserList());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    User getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
