package com.pismo.banking.transaction.internal.exception;

import com.pismo.banking.common.GlobalExceptionHandler;

/**
 * Exception thrown when an invalid operation type is provided.
 *
 * <p>This exception typically results in an HTTP 500 Internal Server Error response being returned to the client,
 * handled by the application's exception handler {@link GlobalExceptionHandler} .</p>
 */
public class InvalidOperationTypeException extends RuntimeException{
    public InvalidOperationTypeException(final int operationTypeId) {
        super(String.format("Operation type id %d is invalid.", operationTypeId));
    }
}
