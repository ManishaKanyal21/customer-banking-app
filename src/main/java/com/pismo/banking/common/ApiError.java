package com.pismo.banking.common;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(int status, String error, String message, LocalDateTime timestamp) {

    public ApiError(HttpStatus status, String message) {
        this(status.value(), status.getReasonPhrase(), message, LocalDateTime.now());
    }
}
