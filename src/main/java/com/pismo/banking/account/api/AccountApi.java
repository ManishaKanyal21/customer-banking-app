package com.pismo.banking.account.api;

import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.common.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Defines the contract for managing customer bank accounts via the REST API.
 * This interface centralizes API documentation and configuration using OpenAPI annotations.
 */
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "Account management APIs")
public interface AccountApi {

    /**
     * Creates a new customer bank account.
     * This operation requires a unique document number to identify the account owner.
     *
     * @param accountRequest The request body containing account details.
     * @return A response entity containing the created account details (HTTP 201 Created).
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new customer account", description = "Requires a unique document number")
    @ApiResponse(responseCode = "201", description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = AccountResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Account already exists",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest);

    /**
     * Retrieves details for an existing account by its unique ID.
     *
     * @param accountId The unique identifier for the account.
     * @return A response entity containing the account details (HTTP 200 OK).
     */
    @GetMapping(value = "/{accountId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve account details by ID")
    @ApiResponse(responseCode = "200", description = "Account details retrieved",
            content = @Content(schema = @Schema(implementation = AccountResponse.class)))
    @ApiResponse(responseCode = "404", description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId);
}
