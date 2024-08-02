package com.nvc.ca_house.mapper;

import com.nvc.ca_house.dto.request.RoleRequest;
import com.nvc.ca_house.dto.response.RoleResponse;
import com.nvc.ca_house.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
