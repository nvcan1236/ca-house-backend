package com.nvc.motel_service.repository.httpclient;

import com.nvc.motel_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "user-client", url = "http://localhost:8080/identity")
public interface UserClient {
    @GetMapping(value = "/users/{userId}")
    UserResponse getUserById(@PathVariable String userId);
}
