package com.nvc.ca_house.service;

import com.nvc.ca_house.dto.request.RoleRequest;
import com.nvc.ca_house.dto.response.RoleResponse;
import com.nvc.ca_house.entity.Permission;
import com.nvc.ca_house.entity.Role;
import com.nvc.ca_house.exception.AppException;
import com.nvc.ca_house.exception.ErrorCode;
import com.nvc.ca_house.mapper.RoleMapper;
import com.nvc.ca_house.repository.PermissionRepository;
import com.nvc.ca_house.repository.RoleRepository;
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
