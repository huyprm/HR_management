package org.ptithcm2021.hr_management.exception;

import lombok.Getter;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED (1000, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_JWT (1001, "Jwt invalid", HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_FOUND(1002, "Username does not existed", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD(1003, "Wrong password", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(1004, "Account locked", HttpStatus.LOCKED)

    ;

    ErrorCode(int code, String message, HttpStatus status){
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
