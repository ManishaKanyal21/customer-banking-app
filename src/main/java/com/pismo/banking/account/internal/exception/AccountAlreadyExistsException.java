package com.pismo.banking.account.internal.exception;

import com.pismo.banking.account.api.controller.AccountExceptionHandler;

/**
 * Exception thrown when an attempt is made to create a new account using a document number
 * that is already associated with an existing account in the system.
 *
 * <p>This exception typically results in an HTTP 409 Conflict response being returned to the client,
 * handled by the application's exception handler {@link AccountExceptionHandler} .</p>
 */
public class AccountAlreadyExistsException extends RuntimeException{

    /**
     * Constructs a new AccountAlreadyExistsException with a detail message.
     *
     * @param documentNumber the documentNumber that already exists.
     */
    public AccountAlreadyExistsException(final String documentNumber) {
        super(String.format("Account with document number %s already exists", documentNumber));
    }
}
