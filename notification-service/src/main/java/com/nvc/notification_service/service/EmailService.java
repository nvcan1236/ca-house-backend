package com.nvc.notification_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvc.notification_service.dto.request.SendEmail;
import com.nvc.notification_service.dto.request.SendEmailRequest;
import com.nvc.notification_service.dto.request.Sender;
import com.nvc.notification_service.dto.response.SendEmailResponse;
import com.nvc.notification_service.enums.TemplateEnum;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class EmailService {
    @Value("${app.mail-api-key}")
    @NonFinal
    String apiKey;

    EmailClient emailClient;
    TemplateEngine templateEngine;

    public String generateEmailContent(TemplateEnum templateName, Map<String, String> contextParams) {
        Context context = new Context();

        contextParams.forEach(context::setVariable);

        return templateEngine.process(templateName.getValue(), context);
    }

    public SendEmailResponse sendEmail(SendEmailRequest request) {
        String htmlContent = generateEmailContent(request.getTemplate(), request.getContextObject());
        SendEmail sendEmail = SendEmail.builder()
                .to(List.of(request.getTo()))
                .htmlContent(htmlContent)
                .sender(Sender.builder()
                        .email("nvcan1236.test@gmail.com")
                        .name("CaHouse System")
                        .build())
                .subject(request.getTemplate().getObject())
                .build();
        try {
            return emailClient.sendEmail(apiKey, sendEmail);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
