package com.nvc.user_service.service;

import com.nvc.user_service.dto.request.ProfileRequest;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.ProfileResponse;
import com.nvc.user_service.entity.Profile;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.mapper.ProfileMapper;
import com.nvc.user_service.mapper.UserMapper;
import com.nvc.user_service.repository.ProfileRepository;
import com.nvc.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {
    private final UserRepository userRepository;
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    UserMapper userMapper;

    public DetailUserResponse update(ProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Profile profile = user.getProfile();
        if(profile == null) {
            profile = new Profile();
            profileRepository.save(profile);
        }
        profileMapper.updateProfile(profile, request);
        user.setProfile(profile);
        userRepository.save(user);
        return userMapper.toDetailUserResponse(user);
    }
}
