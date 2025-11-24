package com.pismo.banking.account.api.controller;

import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.common.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.pismo.banking.account")
public class AccountExceptionHandler {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAccountAlreadyExistsException
            (final AccountAlreadyExistsException accountAlreadyExistsException) {
        final HttpStatus httpStatus = HttpStatus.CONFLICT;
        final ApiError apiError = new ApiError(httpStatus, accountAlreadyExistsException.getMessage());
        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
