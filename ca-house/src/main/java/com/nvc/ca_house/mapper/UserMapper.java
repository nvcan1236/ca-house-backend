package com.nvc.ca_house.mapper;

import com.nvc.ca_house.dto.request.UserCreationRequest;
import com.nvc.ca_house.dto.request.UserUpdateRequest;
import com.nvc.ca_house.dto.response.UserResponse;
import com.nvc.ca_house.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
