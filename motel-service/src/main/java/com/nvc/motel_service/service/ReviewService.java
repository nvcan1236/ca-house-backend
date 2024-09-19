package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.ReviewRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.ReviewResponse;
import com.nvc.motel_service.entity.Review;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.ReviewMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    MotelRepository motelRepository;

    public ReviewResponse create(String motelId, ReviewRequest request) {
        Review review = reviewMapper.toReview(request);
        review.setMotel(motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND))
        );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        review.setCreatedAt(Instant.now());
        review.setMotel(motelRepository.getReferenceById(motelId));
        review.setCreatedBy(username);
        reviewRepository.save(review);
        return reviewMapper.toReviewResponse(review);
    }

    public ReviewResponse update(String id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        reviewMapper.updateReview(review, request);
        reviewRepository.save(review);
        return reviewMapper.toReviewResponse(review);
    }

    public void delete(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        reviewRepository.delete(review);
    }

    public List<ReviewResponse> findAll(String motelId) {
        return reviewRepository.findAllByMotelId(motelId).stream().map(
                reviewMapper::toReviewResponse
        ).toList();
    }

}
