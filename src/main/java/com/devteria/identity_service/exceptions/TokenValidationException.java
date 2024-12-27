package com.devteria.identity_service.exceptions;

public class TokenValidationException extends RuntimeException{
    public TokenValidationException(String message) {
        super(message);
    }
}
