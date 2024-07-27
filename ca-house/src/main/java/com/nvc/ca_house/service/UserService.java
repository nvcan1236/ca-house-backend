package com.nvc.ca_house.service;

import com.nvc.ca_house.dto.request.UserCreationRequest;
import com.nvc.ca_house.dto.request.UserUpdateRequest;
import com.nvc.ca_house.dto.response.UserResponse;
import com.nvc.ca_house.entity.User;
import com.nvc.ca_house.exception.AppException;
import com.nvc.ca_house.exception.ErrorCode;
import com.nvc.ca_house.mapper.UserMapper;
import com.nvc.ca_house.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUserList() {
        return userRepository.findAll()
                .stream().map(user -> userMapper.toUserResponse(user))
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if(user==null) {
            return;
        }
        user.setActive(false);
        userRepository.save(user);
    }
 }
