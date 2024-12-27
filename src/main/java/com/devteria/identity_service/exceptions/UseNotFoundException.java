package com.devteria.identity_service.exceptions;



public class UseNotFoundException extends RuntimeException {
    public UseNotFoundException(String message) {
        super(message);
    }
}
