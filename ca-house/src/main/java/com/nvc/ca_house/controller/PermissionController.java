package com.nvc.ca_house.controller;

import com.nvc.ca_house.dto.request.ApiResponse;
import com.nvc.ca_house.dto.request.PermissionRequest;
import com.nvc.ca_house.dto.response.PermissionResponse;
import com.nvc.ca_house.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.getList());
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {

        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.create(request));
        return apiResponse;
    }

    @DeleteMapping("/{permission}")
    public ApiResponse deactive(@PathVariable String permission) {
        permissionService.deactivate(permission);

        return new ApiResponse<>();
    }
}
