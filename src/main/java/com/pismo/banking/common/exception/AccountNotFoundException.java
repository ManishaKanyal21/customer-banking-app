package com.pismo.banking.common.exception;

public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with id %s not found", accountId));
    }
}
