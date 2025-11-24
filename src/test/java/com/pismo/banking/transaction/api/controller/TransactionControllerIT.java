package com.pismo.banking.transaction.api.controller;

import com.pismo.banking.IntegrationTestBase;
import com.pismo.banking.common.ApiError;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Transaction Controller E2E Tests")
public class TransactionControllerIT extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long existingAccountId;

    @BeforeEach
    public void setUp() {
        if (existingAccountId == null) {
            existingAccountId = createTestAccount(generateUniqueDocNumber());
        }
        assertThat(existingAccountId).isNotNull();
    }

    @Test
    @DisplayName("Should return 201 CREATED for a valid PAYMENT transaction (Type 4)")
    void shouldCreatePaymentTransactionSuccessfully() {
        final Integer operation_type_id = 4;
        final BigDecimal amount = new BigDecimal("123.45");

        TransactionRequest requestPayload = new TransactionRequest(existingAccountId, operation_type_id, amount);

        ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(
                "/transactions",
                requestPayload,
                TransactionResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().operationTypeId()).isNotNull();
        assertThat(response.getBody().accountId()).isEqualTo(existingAccountId);
        assertThat(response.getBody().operationTypeId()).isEqualTo(operation_type_id);
        assertThat(response.getBody().amount()).isEqualByComparingTo(amount);
    }

    @Test
    @DisplayName("Should return 201 CREATED for a valid PURCHASE transaction (Type 1, negative amount)")
    void shouldCreatePurchaseTransactionSuccessfully() {
        final Integer operation_type_id = 1;
        final BigDecimal amount = new BigDecimal("123.45");
        TransactionRequest requestPayload = new TransactionRequest(existingAccountId, operation_type_id, amount);

        ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(
                "/transactions",
                requestPayload,
                TransactionResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().transactionId()).isNotNull();
        assertThat(response.getBody().operationTypeId()).isEqualTo(operation_type_id);
        assertThat(response.getBody().amount()).isEqualByComparingTo(amount.negate());
    }


    @Test
    @DisplayName("Should return 400 BAD REQUEST for a non-existent Operation Type ID")
    void shouldFailForInvalidOperationType() {
        final Integer operation_type_id = 99;
        final BigDecimal amount = new BigDecimal("123.45");
        TransactionRequest requestPayload = new TransactionRequest(existingAccountId, operation_type_id, amount);

        ResponseEntity<ApiError> response = restTemplate.postForEntity(
                "/transactions",
                requestPayload,
                ApiError.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message())
                .contains("Validation failed:")
                .contains("Operation type ID must be at most 4");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when the account ID does not exist")
    void shouldFailForNonExistentAccount() {
        final Long accountId = 9999L;
        final Integer operation_type_id = 4;
        final BigDecimal amount = new BigDecimal("123.45");
        final TransactionRequest requestPayload = new TransactionRequest(accountId, operation_type_id, amount);

        final ResponseEntity<ApiError> response = restTemplate.postForEntity(
                 "/transactions",
                requestPayload,
                ApiError.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message())
                .contains("Account with id 9999 not found");
    }
}
