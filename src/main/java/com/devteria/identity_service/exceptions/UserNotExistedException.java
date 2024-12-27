package com.devteria.identity_service.exceptions;

public class UserNotExistedException extends RuntimeException{
    public UserNotExistedException(String message) {
        super(message);
    }
}
