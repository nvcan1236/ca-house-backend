package com.nvc.user_service.mapper;

import com.nvc.user_service.dto.request.UserCreationRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.UserResponse;
import com.nvc.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target ="roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
    DetailUserResponse toDetailUserResponse(User user);

    @Mapping(target ="roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
