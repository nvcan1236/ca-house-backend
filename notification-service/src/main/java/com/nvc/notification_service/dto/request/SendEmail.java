package com.nvc.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SendEmail {
    Sender sender;
    List<Recipient> to;
    String htmlContent;
    String subject;
}
