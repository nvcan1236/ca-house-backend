package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.controller.MotelImageController;
import com.nvc.motel_service.enums.MotelStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
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
    RequirementResponse requirement;
    List<PriceResponse> prices;
    List<MotelImageResponse> images;
    LocationResponse location;
    String ownerId;
    String type;
    Instant availableDate;
    MotelStatus status;
    String createdAt;
    boolean isApproved;

}
