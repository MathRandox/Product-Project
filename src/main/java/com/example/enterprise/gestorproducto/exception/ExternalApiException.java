package com.example.enterprise.gestorproducto.exception;

public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
