package com.pismo.banking.account.internal.mapper;

import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.internal.model.Account;

import java.util.Objects;

/**
 * A utility class responsible for mapping between the Account entity,
 * AccountRequest DTO, and AccountResponse DTO.
 * This class performs simple data translation.
 */
public final class AccountMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AccountMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Converts an {@link Account} entity object to an {@link AccountResponse} DTO.
     * Maps entity fields to the DTO structure for API responses.
     *
     * @param account The source Account entity, can be null.
     * @return The corresponding AccountResponse DTO, or null if the input is null.
     */
    public static AccountResponse toDto(final Account account) {
        if (Objects.isNull(account)) return null;
        return new AccountResponse(account.getAccountId(), account.getDocumentNumber());
    }

    /**
     * Converts an {@link AccountRequest} DTO into an {@link Account} entity object.
     * Initializes the entity with data provided in the request, typically for creating a new account.
     *
     * @param accountRequest The source AccountRequest DTO.
     * @return The corresponding Account entity, or null if the input is null.
     */
    public static Account toEntity(final AccountRequest accountRequest) {
        return new Account(null, accountRequest.documentNumber());
    }
}
