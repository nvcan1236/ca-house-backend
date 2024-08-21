package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.MotelStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotelResponse {
    String id;
    String ownerId;
    String name;
    Double area;
    Double price;
    String type;
    Date availableDate;
    MotelStatus status;
    Date createdAt;
    List<MotelImageResponse> images;
}
