package com.nvc.motel_service.dto.response;

import com.nvc.motel_service.enums.Job;
import com.nvc.motel_service.enums.PriceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequirementResponse {
    Double deposit;
    int contractAmount;
    boolean allowPet;
    List<Job> jobs;
    String other;
}
