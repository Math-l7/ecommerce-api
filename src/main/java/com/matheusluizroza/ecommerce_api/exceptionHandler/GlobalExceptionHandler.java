package com.matheusluizroza.ecommerce_api.exceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        HttpStatusCode statusCode = ex.getStatusCode(); // HttpStatusCode
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode.value()); // pega o valor numérico
        body.put("error", statusCode.toString()); // converte pra string
        body.put("message", ex.getReason() != null ? ex.getReason() : statusCode.toString());
        return ResponseEntity.status(statusCode.value()).body(body); // converte para int
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro inesperado.");
        return ResponseEntity.status(status).body(body);
    }
}