package com.pismo.banking.account.api.controller;

import com.pismo.banking.account.api.AccountApi;
import com.pismo.banking.account.api.AccountService;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST controller implementation for managing bank accounts.
 * Implements the {@link AccountApi} contract and delegates business logic to the {@link AccountService}.
 */
@RestController
public class AccountController implements AccountApi {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<AccountResponse> createAccount(final AccountRequest accountRequest) {
        final AccountResponse createdAccount = accountService.createAccount(accountRequest);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(createdAccount.accountId())
                .toUri();

        return ResponseEntity.created(location).body(createdAccount);
    }

    @Override
    public ResponseEntity<AccountResponse> getAccount(final Long accountId) {
        final AccountResponse accountById = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountById);
    }
}
