package com.pismo.banking.transaction.api;

import com.pismo.banking.common.exception.AccountNotFoundException;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.exception.InvalidOperationTypeException;

/**
 * Service interface for managing financial transactions.
 * Provides functionality for creating and processing new transactions
 * between accounts or for specific operation types.
 */
public interface TransactionService {

    /**
     * Creates and processes a new transaction based on the details provided in the request.
     * This involves validating account existence, operation type validity,
     * and ensuring correct handling of amounts (positive for credits,
     * negative for debits).
     *
     * @param transactionRequest The request object containing details for the transaction,
     *                           including account ID, operation type ID, and amount.
     * @return A {@link TransactionResponse} object representing the newly created and processed transaction.
     *
     * @throws AccountNotFoundException If the account specified in the request does not exist.
     * @throws InvalidOperationTypeException If the operation type ID provided is invalid or not supported.
     */
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
}
