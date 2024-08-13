package com.nvc.user_service.controller;

import com.nvc.user_service.dto.request.ProfileRequest;
import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.ProfileResponse;
import com.nvc.user_service.service.ProfileService;
import com.nvc.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/profile")
public class ProfileController {
    ProfileService profileService;

    @PutMapping
    ApiResponse<DetailUserResponse> update(@RequestBody ProfileRequest request) {
        return ApiResponse.<DetailUserResponse>builder()
                .result(profileService.update(request))
                .build();
    }
}
