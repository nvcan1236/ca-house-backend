package com.nvc.notification_service.controller;

import com.nvc.event.dto.NotificationEvent;
import com.nvc.notification_service.dto.request.Recipient;
import com.nvc.notification_service.dto.request.SendEmailRequest;
import com.nvc.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent notificationEvent) {
//        log.info("Notification event: {}", notificationEvent);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient
                        .builder()
                        .email(notificationEvent.getRecipient())
                        .build())
                .subject(notificationEvent.getSubject())
                .htmlContent(notificationEvent.getBody())
                .build());
    }
}
