package com.nvc.motel_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //    100x: Account error
    USERNAME_EXISTED(1002, "This username is already existed.", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH_INVALID(1003, "Password must be at least {min} characters.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found.", HttpStatus.BAD_REQUEST),

    //    2xxx: Auth
    UNAUTHENTICATED(2001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2002, "You do not have this permission", HttpStatus.FORBIDDEN),

    // 3xxx: Motel
    MOTEL_NOT_FOUND(3001,"Motel not found.", HttpStatus.BAD_REQUEST),
    INFO_EXIST(3002,"This info for this motel is already exist.", HttpStatus.BAD_REQUEST),


    //    9999: Uncategorized
    NOT_FOUND(1004, "Not found.", HttpStatus.BAD_REQUEST),
    KEY_INVALID_ERROR(9998, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_ERROR(9999, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int errorCode, String message, HttpStatusCode httpStatusCode) {
        this.code = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
