package com.pismo.banking.account.internal.mapper;

import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.internal.model.Account;

import java.util.Objects;

public final class AccountMapper {

    private AccountMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static AccountResponse toDto(final Account account) {
        if (Objects.isNull(account)) return null;
        return new AccountResponse(account.getAccountId(), account.getDocumentNumber());
    }

    public static Account toEntity(final AccountRequest accountRequest) {
        if (Objects.isNull(accountRequest)) return null;
        return new Account(null, accountRequest.documentNumber());
    }
}
