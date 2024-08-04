package com.nvc.ca_house.controller;

import com.nimbusds.jose.JOSEException;
import com.nvc.ca_house.dto.request.ApiResponse;
import com.nvc.ca_house.dto.request.AuthenticationRequest;
import com.nvc.ca_house.dto.request.IntrospectRequest;
import com.nvc.ca_house.dto.request.LogoutRequest;
import com.nvc.ca_house.dto.response.AuthenticationResponse;
import com.nvc.ca_house.dto.response.IntrospectResponse;
import com.nvc.ca_house.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
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
}
