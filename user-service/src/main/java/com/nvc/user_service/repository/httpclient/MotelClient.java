package com.nvc.user_service.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient(name = "", url = "")
public interface MotelClient {
    @GetMapping(value = "")
    public Object getUserMotel();
}
