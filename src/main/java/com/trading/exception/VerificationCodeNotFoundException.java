package com.trading.exception;

public class VerificationCodeNotFoundException extends RuntimeException {

    public VerificationCodeNotFoundException(String message){
        super(message);
    }
}
