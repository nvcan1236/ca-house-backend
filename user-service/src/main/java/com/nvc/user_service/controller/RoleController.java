package com.nvc.user_service.controller;

import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.dto.request.RoleRequest;
import com.nvc.user_service.dto.response.RoleResponse;
import com.nvc.user_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.getList());
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.create(request));
        return apiResponse;
    }
    @DeleteMapping("/{role}")
    public ApiResponse deactive(@PathVariable String role) {
        roleService.deactivate(role);

        return new ApiResponse<>();
    }

}
