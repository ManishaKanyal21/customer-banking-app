package com.pismo.banking.transaction.internal.mapper;

import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.model.OperationType;
import com.pismo.banking.transaction.internal.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 * A utility class responsible for mapping between Transaction entities and DTOs.
 * This class performs simple data translation and does not contain business logic regarding transaction amounts.
 */
public final class TransactionMapper {

    private TransactionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Converts a {@link Transaction} entity object to a {@link TransactionResponse} DTO.
     * Maps entity fields to the DTO structure for API responses.
     *
     * @param transaction The source Transaction entity, can be null.
     * @return The corresponding TransactionResponse , or null if the input is null.
     */
    public static TransactionResponse toDto(final Transaction transaction) {
        if (Objects.isNull(transaction)) return null;
        return new TransactionResponse(transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getOperationType().getId(),
                transaction.getAmount());
    }

    /**
     * Converts raw transaction details into a {@link Transaction} entity object, ready for persistence.
     * The final amount (signed correctly) and the resolved OperationType must be provided.
     *
     * @param accountId     The ID of the account involved.
     * @param operationType The resolved {@link OperationType}.
     * @param amount        The final calculated amount (positive for credits, negative for debits).
     * @return The corresponding Transaction entity.
     */
    public static Transaction toEntity(final Long accountId, final OperationType operationType, final BigDecimal amount) {
        return new Transaction(
                null,
                accountId,
                operationType,
                amount,
                LocalDateTime.now()
        );
    }
}
