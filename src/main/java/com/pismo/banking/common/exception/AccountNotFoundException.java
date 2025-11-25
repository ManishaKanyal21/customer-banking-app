package com.pismo.banking.common.exception;

import com.pismo.banking.common.GlobalExceptionHandler;

/**
 * Exception thrown when an attempt is made to retrieve or operate on an account
 * that does not exist within the system based on the provided account identifier (ID).
 *
 * <p>This exception typically results in an HTTP 404 Not Found response being returned to the client,
 * handled by the application's exception handler {@link GlobalExceptionHandler}.</p>
 */
public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with id %s not found", accountId));
    }
}
