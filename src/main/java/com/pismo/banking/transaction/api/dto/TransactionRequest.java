package com.pismo.banking.transaction.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing the request body required to create a transaction.
 * This record enforces specific validation rules for the transaction fields.
 */
@Schema(description = "Request body for creating a transaction")
public record TransactionRequest(
        /**
         * The unique system-generated identifier for the account.
         */
        @NotNull(message = "Account ID is required")
        @Positive(message = "Account ID must be positive")
        @JsonProperty("account_id")
        @Schema(description = "Account identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long accountId,
        /**
         * The operationType id for the operation performed by customer.
         */
        @NotNull(message = "Operation type ID is required")
        @Min(value = 1, message = "Operation type ID must be at least 1")
        @Max(value = 4, message = "Operation type ID must be at most 4")
        @JsonProperty("operation_type_id")
        @Schema(description = "Operation type (1=PURCHASE, 2=INSTALLMENT_PURCHASE, 3=WITHDRAWAL, 4=PAYMENT)",
                example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer operationTypeId,
        /**
         * The transaction amount.
         */
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive, non-zero value.")
        @Digits(integer = 10, fraction = 2, message = "Amount must have a maximum of 2 fractional digits")
        @Schema(description = "Transaction amount (always positive, sign is determined by operation type)",
                example = "123.45", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal amount
) {
}
