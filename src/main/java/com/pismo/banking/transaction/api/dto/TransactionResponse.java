package com.pismo.banking.transaction.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Transaction response")
public record TransactionResponse(
        @JsonProperty("transaction_id")
        @Schema(description = "Unique transaction identifier", example = "1")
        Long transactionId,
        @JsonProperty("account_id")
        @Schema(description = "Account identifier", example = "1")
        Long accountId,
        @JsonProperty("operation_type_id")
        @Schema(description = "Operation type identifier", example = "4")
        Integer operationTypeId,
        @Schema(description = "Transaction amount", example = "123.45")
        BigDecimal amount) {
}
