package com.pismo.banking.account.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "Account response")
public record AccountResponse(
        @JsonProperty("account_id")
        @Schema(description = "Unique account identifier", example = "1")
        Long accountId,
        @JsonProperty("document_number")
        @Schema(description = "Customer's document number", example = "12345678900")
        String documentNumber) {
}
