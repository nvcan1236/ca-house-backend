package com.nvc.user_service.mapper;

import com.nvc.user_service.dto.request.ProfileRequest;
import com.nvc.user_service.dto.request.RoleRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.ProfileResponse;
import com.nvc.user_service.dto.response.RoleResponse;
import com.nvc.user_service.entity.Profile;
import com.nvc.user_service.entity.Role;
import com.nvc.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileRequest request);
    ProfileResponse toProfileResponse(Profile profile);

    void updateProfile(@MappingTarget Profile profile, ProfileRequest request);
}
