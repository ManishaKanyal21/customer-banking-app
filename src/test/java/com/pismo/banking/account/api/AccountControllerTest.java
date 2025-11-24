package com.pismo.banking.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pismo.banking.account.api.controller.AccountController;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.common.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AccountService accountService;

    @Test
    void whenPostAccount_thenReturnsCreatedAccount() throws Exception {
        final String documentNumber = "12345678900";
        AccountRequest request = new AccountRequest(documentNumber);

        AccountResponse mockedResponse = new AccountResponse(1L, documentNumber);

        when(accountService.createAccount(request)).thenReturn(mockedResponse);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "http://localhost/accounts/1"))
                .andExpect(jsonPath("$.account_id").value(1))
                .andExpect(jsonPath("$.document_number").value(documentNumber));
    }

    @Test
    void whenPostAccountWithInvalidBody_thenReturnsBadRequest() throws Exception {
        String invalidJson = "{\"document_number\": \"invalid_short\"}";

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value("Validation failed: documentNumber: Document number must be 11 digits"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPostAccountThatAlreadyExists_thenReturnsConflict() throws Exception {
        final String documentNumber = "12345678900";
        AccountRequest request = new AccountRequest(documentNumber);

        when(accountService.createAccount(request))
                .thenThrow(new AccountAlreadyExistsException(documentNumber));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(String.format("Account with document number %s already exists", documentNumber)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPostAccountAndServiceFailsInternally_thenReturnsInternalServerError() throws Exception {
        final String documentNumber = "12345678900";
        final AccountRequest request = new AccountRequest(documentNumber);

        when(accountService.createAccount(request))
                .thenThrow(new DataAccessException("Database connection failed") {});

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Database connection failed"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenGetAccountAndServiceFailsInternally_thenReturnsInternalServerError() throws Exception {
        final Long accountId = 1L;

        when(accountService.getAccountById(accountId))
                .thenThrow(new RuntimeException("An unexpected internal error occurred."));

        mockMvc.perform(get("/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected internal error occurred."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenGetAccountById_thenReturnsAccountDetails() throws Exception {
        final Long accountId = 1L;
        final String documentNumber = "12345678900";

        final AccountResponse mockedResponse = new AccountResponse(accountId, documentNumber);

        when(accountService.getAccountById(accountId)).thenReturn(mockedResponse);

        mockMvc.perform(get("/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id").value(accountId))
                .andExpect(jsonPath("$.document_number").value(documentNumber));
    }

    @Test
    void whenGetAccountByIdNonExistent_thenReturnsNotFound() throws Exception {
        final Long nonExistentId = 99L;

        when(accountService.getAccountById(nonExistentId)).thenThrow(new AccountNotFoundException(nonExistentId));

        mockMvc.perform(get("/accounts/{accountId}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(String.format("Account with id %s not found", nonExistentId)))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
