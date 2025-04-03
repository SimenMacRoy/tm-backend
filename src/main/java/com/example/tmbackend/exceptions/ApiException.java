package com.example.tmbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiException(HttpStatus status, String message, String error) {

    public ResponseEntity<ApiException> toResponseEntity() {
        return new ResponseEntity<>(this, status);
    }
}
