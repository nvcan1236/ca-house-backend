package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.request.RequirementRequest;
import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.dto.response.RequirementResponse;
import com.nvc.motel_service.entity.Requirement;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.RequirementMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.RequirementRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class RequirementService {
    RequirementRepository requirementRepository;
    RequirementMapper requirementMapper;
    MotelRepository motelRepository;

    public void create(String motelId, RequirementRequest request) {
        Requirement requirement = requirementMapper.toRequirement(request);
        requirement.setMotel(motelRepository.findById(motelId)
                .orElseThrow(()-> new AppException(ErrorCode.MOTEL_NOT_FOUND)));
        requirementRepository.save(requirement);
    }

    public void update(String id, RequirementRequest request) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        requirementMapper.updateRequirement(requirement, request);
        requirementRepository.save(requirement);
    }

    public void delete(String id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        requirementRepository.delete(requirement);
    }

}
