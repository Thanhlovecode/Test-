package com.devteria.identity_service.exceptions;

public class UserExistedException extends RuntimeException{
    public UserExistedException(String message) {
        super(message);
    }
}
