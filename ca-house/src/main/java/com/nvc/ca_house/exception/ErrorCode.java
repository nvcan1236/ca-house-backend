package com.nvc.ca_house.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //    100x: Account error
    USERNAME_EXISTED(1002, "This username is already existed."),
    PASSWORD_LENGTH_INVALID(1003, "Password must be at least 8 characters."),
    USER_NOT_FOUND(1004, "User not found."),

    //    2xxx: Auth
    UNAUTHENTICATED(2001, "Unauthenticated"),
    UNAUTHORIZED(2002, "Unauthorized"),


    //    9999: Uncategorized
    KEY_INVALID_ERROR(9998, "Uncategorized error."),
    UNCATEGORIZED_ERROR(9999, "Uncategorized error.");

    ErrorCode(int errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }

    private int code;
    private String message;
}
