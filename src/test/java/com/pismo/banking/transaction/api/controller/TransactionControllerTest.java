package com.pismo.banking.transaction.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pismo.banking.common.exception.AccountNotFoundException;
import com.pismo.banking.transaction.api.TransactionService;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.exception.InvalidOperationTypeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void whenPostTransactionValid_thenReturnsCreatedTransaction() throws Exception {
        final Long account_id = 1L;
        final Integer operation_type_id = 4; // PAYMENT
        final BigDecimal amount = new BigDecimal("123.45");
        final TransactionRequest request = new TransactionRequest(account_id, operation_type_id, amount);

        final TransactionResponse mockResponse = new TransactionResponse(1L, account_id, operation_type_id, amount);

        when(transactionService.createTransaction(request)).thenReturn(mockResponse);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transaction_id").value(1L))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.operation_type_id").value(operation_type_id));
    }

    @Test
    void whenPostTransactionInvalidAccountId_thenReturnsBadRequest() throws Exception {
        final Long nonExistentId = 999L;
        final Integer operation_type_id = 4; // PAYMENT
        final BigDecimal amount = new BigDecimal(50.0);
        final TransactionRequest request = new TransactionRequest(nonExistentId, operation_type_id, amount);

        when(transactionService.createTransaction(request))
                .thenThrow(new AccountNotFoundException(nonExistentId));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Account with id %s not found", nonExistentId)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPostTransactionInvalidOperationType_thenReturnsBadRequest() throws Exception {
        final Long nonExistentId = 1L;
        final Integer invalidOperationType = 99;
        final BigDecimal amount = new BigDecimal(50.0);
        final TransactionRequest request = new TransactionRequest(nonExistentId, invalidOperationType, amount);

        when(transactionService.createTransaction(request))
                .thenThrow(new InvalidOperationTypeException(99));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value("Validation failed: operationTypeId: Operation type ID must be at most 4"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPostTransactionAndServiceFailsInternally_thenReturnsInternalServerError() throws Exception {
        final Long account_id = 1L;
        final Integer operation_type_id = 4;
        final BigDecimal amount = new BigDecimal("123.45");
        final TransactionRequest request = new TransactionRequest(account_id, operation_type_id, amount);

        when(transactionService.createTransaction(request))
                .thenThrow(new RuntimeException("Database error."));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Database error."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}