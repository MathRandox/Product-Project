package com.example.enterprise.gestorproducto.exception;

import com.example.enterprise.gestorproducto.exception.model.GlobalErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({BusinessException.class, DatabaseException.class, ExternalApiException.class, ResourceNotFoundException.class})
    public Mono<ResponseEntity<GlobalErrorResponse>> handleCustomException(RuntimeException ex) {
        GlobalErrorResponse error = GlobalErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .technicalDetails(ex.getClass().getSimpleName())
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<GlobalErrorResponse>> handleGenericException(Exception ex) {
        GlobalErrorResponse error = GlobalErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected error occurred")
                .technicalDetails(ex.getMessage())
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
