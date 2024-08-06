package com.nvc.user_service.mapper;

import com.nvc.user_service.dto.request.PermissionRequest;
import com.nvc.user_service.dto.response.PermissionResponse;
import com.nvc.user_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse (Permission permission);
}
