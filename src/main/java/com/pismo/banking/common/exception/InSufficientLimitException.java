package com.pismo.banking.common.exception;

public class InSufficientLimitException extends RuntimeException {
    public InSufficientLimitException(final String message) {
        super(message);
    }
}
