package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.controller.MotelImageController;
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
public class DetailMotelResponse {
    String id;
    String name;
    String description;
    Double area;
    Double price;
    List<AmenityResponse> amenities;
    List<RequirementResponse> requirements;
    List<PriceResponse> prices;
    List<MotelImageResponse> images;
    LocationResponse location;
    String ownerId;
    String type;
    Date availableDate;
    MotelStatus status;
    Date createdAt;
}
