package com.nvc.user_service.controller;

import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.dto.request.UserCreationRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.UserResponse;
import com.nvc.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUserList() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<List<UserResponse>>();
        apiResponse.setResult(userService.getUserList());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));

        return apiResponse;
    }

    @GetMapping("/me")
    ApiResponse<UserResponse> getCurrentUser() {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getCurrentUser());

        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
