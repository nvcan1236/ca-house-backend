package com.nvc.user_service.service;

import com.nvc.user_service.dto.request.PermissionRequest;
import com.nvc.user_service.dto.response.PermissionResponse;
import com.nvc.user_service.entity.Permission;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.mapper.PermissionMapper;
import com.nvc.user_service.repository.PermissionRepository;
import com.nvc.user_service.validator.AdminOnly;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @AdminOnly
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @AdminOnly
    public List<PermissionResponse> getList() {
        return permissionRepository
                .findAll()
                .stream().map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @AdminOnly
    public void deactivate(String permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        permission.setActive(false);
        permissionRepository.save(permission);
    }
}
