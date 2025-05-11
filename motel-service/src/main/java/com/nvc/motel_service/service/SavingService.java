package com.nvc.motel_service.service;

import com.nvc.motel_service.dto.response.MotelResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Saving;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.MotelMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.SavingRepository;
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
public class SavingService {
    SavingRepository savingRepository;
    MotelRepository motelRepository;
    MotelMapper motelMapper;

    public Saving checkMotelSavedBy(String motelId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return savingRepository.findByUserIdAndMotel_Id(username, motelId);
    }

    public List<MotelResponse> getSavedMotels() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return savingRepository.findAllByUserIdAndIsActive(username, true)
                .stream()
                .map(saving -> motelMapper.toMotelResponse(saving.getMotel()))
                .toList();
    }

    public boolean saveMotel(String motelId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Saving savingMotel = checkMotelSavedBy(motelId);
        if (savingMotel != null) {
            savingMotel.setIsActive(!savingMotel.getIsActive());
            savingRepository.save(savingMotel);
            return savingMotel.getIsActive();
        } else {
            Motel motel = motelRepository.findById(motelId).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
            Saving newSaving = new Saving();
            newSaving.setMotel(motel);
            newSaving.setCreatedAt(Instant.now());
            newSaving.setIsActive(true);
            newSaving.setUserId(username);

            savingRepository.save(newSaving);
            return true;
        }
    }
}
