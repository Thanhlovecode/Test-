package com.devteria.identity_service.exceptions;

public class UnAuthenticationException  extends RuntimeException{
    public UnAuthenticationException(String message) {
        super(message);
    }
}
