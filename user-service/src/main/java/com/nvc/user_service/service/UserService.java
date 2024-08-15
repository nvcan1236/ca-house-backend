package com.nvc.user_service.service;

import com.nvc.event.dto.NotificationEvent;
import com.nvc.user_service.dto.request.UserCreationRequest;
import com.nvc.user_service.dto.request.UserUpdateRequest;
import com.nvc.user_service.dto.response.DetailUserResponse;
import com.nvc.user_service.dto.response.UserResponse;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.mapper.UserMapper;
import com.nvc.user_service.repository.RoleRepository;
import com.nvc.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    private final RoleRepository roleRepository;

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to CaHouse")
                .body("Bạn vừa đăng ký tài khoản tại ca-house với username: " + request.getUsername())
                .build();

        kafkaTemplate.send("notification-delivery", notificationEvent);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUserList() {
        log.info("Role: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return userRepository.findAll()
                .stream().map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED));

        UserResponse response =  userMapper.toUserResponse(user);
        response.setNoPassword(!StringUtils.hasText(user.getPassword()));
        return response;
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public DetailUserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDetailUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserResponse> getFollower(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return user.getFollower().stream().map(userMapper::toUserResponse).toList();
    }

    public List<UserResponse> getFollowing(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return user.getFollowing().stream().map(userMapper::toUserResponse).toList();
    }

    public void follow(String userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED));

        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        currentUser.getFollowing().add(user);
        currentUser.setFollowing(currentUser.getFollowing());
        userRepository.save(currentUser);
    }
}
