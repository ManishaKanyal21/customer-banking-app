package com.pismo.banking.account.api;

import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.api.dto.AccountRequest;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccountById(Long accountId);

    void validateAccountExists(Long accountId);
}
