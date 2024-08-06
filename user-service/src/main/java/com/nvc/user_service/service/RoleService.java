package com.nvc.user_service.service;

import com.nvc.user_service.dto.request.RoleRequest;
import com.nvc.user_service.dto.response.RoleResponse;
import com.nvc.user_service.entity.Role;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.mapper.RoleMapper;
import com.nvc.user_service.repository.PermissionRepository;
import com.nvc.user_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    private final PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getList() {
        return roleRepository
                .findAll()
                .stream().map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deactivate(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        role.setActive(false);
        roleRepository.save(role);
    }
}
