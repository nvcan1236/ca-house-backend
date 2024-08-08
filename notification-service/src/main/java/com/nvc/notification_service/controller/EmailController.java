package com.nvc.notification_service.controller;

import com.nvc.notification_service.dto.request.SendEmailRequest;
import com.nvc.notification_service.dto.response.ApiResponse;
import com.nvc.notification_service.dto.response.SendEmailResponse;
import com.nvc.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;
    @PostMapping("/email/send")
    public ApiResponse<SendEmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
        return ApiResponse.<SendEmailResponse>builder()
                .result(emailService.sendEmail(request))
                .build();
    }


}
