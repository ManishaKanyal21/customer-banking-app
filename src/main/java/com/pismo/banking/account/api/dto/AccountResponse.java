package com.pismo.banking.account.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Data Transfer Object (DTO) representing the response body for account details.
 */
@Schema(description = "Account response")
public record AccountResponse(
        /**
         * The unique system-generated identifier for the account.
         */
        @JsonProperty("account_id")
        @Schema(description = "Unique account identifier", example = "1")
        Long accountId,
        /**
         * The customer's unique 11-digit document number used during account creation.
         */
        @JsonProperty("document_number")
        @Schema(description = "Customer's document number", example = "12345678900")
        String documentNumber) {
}
