package com.pismo.banking.account.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * A Data Transfer Object (DTO) representing the request body required to create a new bank account.
 * This record enforces specific validation rules for the account fields.
 */
@Schema(description = "Request body for creating a new account")
public record AccountRequest(
        /**
         * The unique identification number of the account owner.
         * Must be an 11-digit numeric string and cannot be blank.
         */
        @JsonProperty("document_number")
        @NotBlank(message = "Document number is required")
        @Pattern(regexp = "^\\d{11}$", message = "Document number must be 11 digits")
        @Schema(name="document_number", description = "Customer's document number", example = "12345678900", requiredMode = Schema.RequiredMode.REQUIRED)
        String documentNumber) {
}
