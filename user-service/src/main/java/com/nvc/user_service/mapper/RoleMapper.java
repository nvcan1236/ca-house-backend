package com.nvc.user_service.mapper;

import com.nvc.user_service.dto.request.RoleRequest;
import com.nvc.user_service.dto.response.RoleResponse;
import com.nvc.user_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
