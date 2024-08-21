package com.nvc.user_service.controller;

import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.dto.request.UserCreationRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.UserResponse;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.repository.httpclient.FileClient;
import com.nvc.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getList() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<List<UserResponse>>();
        apiResponse.setResult(userService.getUserList());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<DetailUserResponse> getById(@PathVariable String userId) {
        ApiResponse<DetailUserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));

        return apiResponse;
    }

    @GetMapping("/my-infor")
    ApiResponse<UserResponse> getCurrent() {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getCurrentUser());

        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> update(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    String delete(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @GetMapping("/{userId}/follower")
    public ApiResponse<List<UserResponse>> getFollower(@PathVariable String userId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getFollower(userId))
                .build();
    }

    @GetMapping("/{userId}/following")
    public ApiResponse<List<UserResponse>> getFollowing(@PathVariable String userId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getFollowing(userId))
                .build();
    }

    @PostMapping("/{userId}/follow")
    public ApiResponse<String> follow(@PathVariable String userId) {
        userService.follow(userId);
        return ApiResponse.<String>builder()
                .result("Followed")
                .build();
    }

    @PostMapping("/upload-avatar")
    public ApiResponse<String> uploadAvatar(@RequestParam("images") List<MultipartFile> files) {
        log.info(files.toString());
        if(CollectionUtils.isEmpty(files))
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        String url = userService.uploadAvatar(files);

        return ApiResponse.<String>builder()
                .result(url)
                .message("Cập nhật ảnh đại diện thành công")
                .build();
    }
}
