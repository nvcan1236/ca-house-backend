package com.nvc.user_service.controller;

import com.nimbusds.jose.JOSEException;
import com.nvc.user_service.dto.request.*;
import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.dto.response.AuthenticationResponse;
import com.nvc.user_service.dto.response.IntrospectResponse;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.repository.UserRepository;
import com.nvc.user_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    private final UserRepository userRepository;
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticateUser(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspectToken(request.getToken());
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var response = authenticationService.refresh(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/create-password")
    public ApiResponse<String> createPassword(@RequestBody CreatePasswordRequest request) {
        authenticationService.createPassword(request);
        return ApiResponse.<String>builder()
                .result("Password has been created successfully")
                .build();
    }

    @PostMapping("/outbound/authentication")
    public ApiResponse<AuthenticationResponse> outboundAuthenticate(@RequestParam String code) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.exchangeToken(code))
                .build();
    }
}
