package com.nvc.motel_service.mapper;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.ReviewRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.ReviewResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview (ReviewRequest request);
    ReviewResponse toReviewResponse(Review review);
    void updateReview(@MappingTarget Review review, ReviewRequest request);
}
