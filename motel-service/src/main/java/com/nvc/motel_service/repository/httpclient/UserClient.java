package com.nvc.motel_service.repository.httpclient;

import com.nvc.motel_service.dto.response.ApiResponse;
import com.nvc.motel_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-client", url = "${app.user-endpoint}/identity")
public interface UserClient {
    @GetMapping(value = "/users/{userId}/short")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId);
}
