package com.example.enterprise.gestorproducto.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalErrorResponse{
    private LocalDateTime timestamp;
    private String message;
    private String technicalDetails;
}
