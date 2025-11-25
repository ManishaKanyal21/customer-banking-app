package com.pismo.banking.transaction.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing the response body for transaction details.
 */
@Schema(description = "Transaction response")
public record TransactionResponse(
        /**
         * The unique system-generated identifier for the transaction.
         */
        @JsonProperty("transaction_id")
        @Schema(description = "Unique transaction identifier", example = "1")
        Long transactionId,
        /**
         * The unique system-generated identifier for the account.
         */
        @JsonProperty("account_id")
        @Schema(description = "Account identifier", example = "1")
        Long accountId,
        /**
         * The operationType id for the operation performed by customer.
         */
        @JsonProperty("operation_type_id")
        @Schema(description = "Operation type identifier", example = "4")
        Integer operationTypeId,
        /**
         * The transaction amount.
         */
        @Schema(description = "Transaction amount", example = "123.45")
        BigDecimal amount) {
}
