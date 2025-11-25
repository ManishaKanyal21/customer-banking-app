package com.pismo.banking.account.api.controller;

import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.common.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler specifically for exceptions originating within the 'com.pismo.banking.account' package controllers.
 * This class uses the @ControllerAdvice annotation to intercept and process exceptions into standardized API error responses ({@link ApiError}).
 */
@ControllerAdvice("com.pismo.banking.account")
public class AccountExceptionHandler {

    /**
     * Exception handler specifically for {@link AccountAlreadyExistsException}.
     * This method translates the custom business exception into an HTTP 409 Conflict response with a standardized error body.
     *
     * @param accountAlreadyExistsException The exception that was thrown during request processing.
     * @return A {@link ResponseEntity} containing the {@link ApiError} object and an HTTP 409 (CONFLICT) status.
     */
    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAccountAlreadyExistsException
            (final AccountAlreadyExistsException accountAlreadyExistsException) {
        final HttpStatus httpStatus = HttpStatus.CONFLICT;
        final ApiError apiError = new ApiError(httpStatus, accountAlreadyExistsException.getMessage());
        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
