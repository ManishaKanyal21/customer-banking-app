package com.pismo.banking.common;

import com.pismo.banking.common.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(httpStatus, "Validation failed: " + errorMessage);
        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleGenericRuntimeException(RuntimeException exception) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = new ApiError(httpStatus, exception.getMessage());
        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFoundException
            (final AccountNotFoundException accountNotFoundException) {
        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ApiError apiError = new ApiError(httpStatus, accountNotFoundException.getMessage());
        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
