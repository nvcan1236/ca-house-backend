package com.nvc.user_service.mapper;

import com.nvc.user_service.dto.request.UserCreationRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.UserResponse;
import com.nvc.user_service.entity.Role;
import com.nvc.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    DetailUserResponse toDetailUserResponse(User user);

    @Named("mapRolesToNames")
    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
