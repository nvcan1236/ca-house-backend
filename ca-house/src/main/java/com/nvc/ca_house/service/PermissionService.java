package com.nvc.ca_house.service;

import com.nvc.ca_house.dto.request.PermissionRequest;
import com.nvc.ca_house.dto.response.PermissionResponse;
import com.nvc.ca_house.entity.Permission;
import com.nvc.ca_house.exception.AppException;
import com.nvc.ca_house.exception.ErrorCode;
import com.nvc.ca_house.mapper.PermissionMapper;
import com.nvc.ca_house.repository.PermissionRepository;
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

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getList() {
        return permissionRepository
                .findAll()
                .stream().map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void deactivate(String permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        permission.setActive(false);
        permissionRepository.save(permission);
    }
}
