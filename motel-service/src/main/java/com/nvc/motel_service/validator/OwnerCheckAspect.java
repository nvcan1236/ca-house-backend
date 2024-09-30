package com.nvc.motel_service.validator;

import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.service.MotelService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class OwnerCheckAspect {

    MotelService motelService;


    @Before("@annotation(com.nvc.motel_service.validator.OwnerOnly) && args(motelId,..)")
    public void checkOwnership(String motelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (!motelService.isOwner(motelId, currentUsername)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
