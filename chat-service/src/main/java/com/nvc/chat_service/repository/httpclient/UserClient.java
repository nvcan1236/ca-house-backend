package com.nvc.chat_service.repository.httpclient;

import com.nvc.chat_service.dto.ApiResponse;
import com.nvc.chat_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-client", url = "${app.user-endpoint}/identity")
public interface UserClient {
    @GetMapping(value = "/users/{userId}/short")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId);
}
