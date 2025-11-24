package com.pismo.banking.transaction.internal.exception;

public class InvalidOperationTypeException extends RuntimeException{
    public InvalidOperationTypeException(final int operationTypeId) {
        super(String.format("Operation type id %d is invalid.", operationTypeId));
    }
}
