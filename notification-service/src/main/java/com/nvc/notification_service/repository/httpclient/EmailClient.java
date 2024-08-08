package com.nvc.notification_service.repository.httpclient;

import com.nvc.notification_service.dto.request.SendEmail;
import com.nvc.notification_service.dto.request.SendEmailRequest;
import com.nvc.notification_service.dto.response.SendEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email-client", url = "https://api.brevo.com")
public interface EmailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    SendEmailResponse sendEmail(@RequestHeader("api-key") String apiKey,
                                @RequestBody SendEmail request);
}
