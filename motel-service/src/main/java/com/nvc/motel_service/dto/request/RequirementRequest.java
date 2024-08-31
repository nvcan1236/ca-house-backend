package com.nvc.motel_service.dto.request;

import com.nvc.motel_service.enums.Job;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequirementRequest {
    Double deposit;
    int contractAmount;
    boolean allowPet;
    List<Job> jobs;
    String other;

}
