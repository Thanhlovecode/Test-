package com.devteria.identity_service.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    USER_NOT_FOUND("User not found",HttpStatus.NOT_FOUND),
    USER_EXISTED("User existed",HttpStatus.CONFLICT),
    UNAUTHENTICATED("Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You don't have permission",HttpStatus.FORBIDDEN),
    TOKEN_INVALID("Invalid token",HttpStatus.UNAUTHORIZED)
    ;
    private final String message;
    private final  HttpStatus httpStatus;

    ErrorCode(String message,HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
