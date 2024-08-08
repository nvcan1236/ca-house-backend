package com.nvc.notification_service.service;

import com.nvc.notification_service.dto.request.SendEmail;
import com.nvc.notification_service.dto.request.SendEmailRequest;
import com.nvc.notification_service.dto.request.Sender;
import com.nvc.notification_service.dto.response.SendEmailResponse;
import com.nvc.notification_service.exception.AppException;
import com.nvc.notification_service.exception.ErrorCode;
import com.nvc.notification_service.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class EmailService {
    @Value("${app.mail-api-key}")
    @NonFinal
    String apiKey;

    EmailClient emailClient;

    public SendEmailResponse sendEmail(SendEmailRequest request) {
        SendEmail sendEmail = SendEmail.builder()
                .to(List.of(request.getTo()))
                .htmlContent(request.getHtmlContent())
                .sender(Sender.builder()
                        .email("ngcanh1236@gmail.com")
                        .name("Nguyen Van Canh")
                        .build())
                .subject(request.getSubject())
                .build();
        try {
            return emailClient.sendEmail(apiKey, sendEmail);
        }
        catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
