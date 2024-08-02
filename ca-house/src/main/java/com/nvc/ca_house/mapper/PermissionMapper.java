package com.nvc.ca_house.mapper;

import com.nvc.ca_house.dto.request.PermissionRequest;
import com.nvc.ca_house.dto.response.PermissionResponse;
import com.nvc.ca_house.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse (Permission permission);
}
