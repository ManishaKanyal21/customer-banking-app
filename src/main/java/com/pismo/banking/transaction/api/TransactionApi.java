package com.pismo.banking.transaction.api;

import com.pismo.banking.common.ApiError;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the contract for processing financial transactions via the REST API.
 * This interface centralizes API documentation and configuration using OpenAPI annotations.
 */
@RequestMapping("/transactions")
@Tag(name = "Transaction Management", description = "Transaction management APIs")
public interface TransactionApi {

    /**
     * Creates and processes a new financial transaction against a customer account.
     * This operation applies business logic to validate inputs and determine the
     * final positive or negative sign of the transaction amount based on the operation type ID.
     *
     * @param transactionRequest The request body containing the transaction details like account ID, operation type ID, and amount.
     * @return A response entity containing the created transaction details (HTTP 201 CREATED).
     */
    @PostMapping
    @Operation(summary = "Create a new transaction for an account")
    @ApiResponse(responseCode = "201", description = "Transaction created successfully",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Account doesn't exist",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest);
}
