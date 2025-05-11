package com.nvc.notification_service.dto.request;

import com.nvc.notification_service.enums.TemplateEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SendEmailRequest {
    Recipient to;
    TemplateEnum template;
    Map<String, String> contextObject;
}
