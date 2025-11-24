package com.pismo.banking.account.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for creating a new account")
public record AccountRequest(
        @JsonProperty("document_number")
        @NotBlank(message = "Document number is required.")
        @Pattern(regexp = "^\\d{11}$", message = "Document number must be 11 digits")
        @Schema(name="document_number", description = "Customer's document number", example = "12345678900", requiredMode = Schema.RequiredMode.REQUIRED)
        String documentNumber) {
}
