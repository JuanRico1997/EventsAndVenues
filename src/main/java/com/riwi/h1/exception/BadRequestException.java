package com.riwi.h1.exception;

public class BadRequestException extends RuntimeException {


    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
