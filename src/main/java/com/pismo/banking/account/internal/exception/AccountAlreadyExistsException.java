package com.pismo.banking.account.internal.exception;

public class AccountAlreadyExistsException extends RuntimeException{

    public AccountAlreadyExistsException(final String documentNumber) {
        super(String.format("Account with document number %s already exists", documentNumber));
    }
}
