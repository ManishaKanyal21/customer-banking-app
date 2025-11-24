package com.pismo.banking.transaction.internal.mapper;

import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.model.OperationType;
import com.pismo.banking.transaction.internal.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class TransactionMapper {

    private TransactionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static TransactionResponse toDto(final Transaction transaction) {
        if (Objects.isNull(transaction)) return null;
        return new TransactionResponse(transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getOperationType().getId(),
                transaction.getAmount());
    }

    public static Transaction toEntity(final TransactionRequest transactionRequest) {
        if (Objects.isNull(transactionRequest)) return null;

        final OperationType operationType = OperationType.fromId(transactionRequest.operationTypeId());
        final BigDecimal amount = transactionRequest.amount();
        final boolean isPayment = operationType == OperationType.PAYMENT;

        final BigDecimal finalAmount = isPayment ? amount : amount.negate();

        return new Transaction(
                null,
                transactionRequest.accountId(),
                operationType,
                finalAmount,
                LocalDateTime.now()
        );
    }
}
