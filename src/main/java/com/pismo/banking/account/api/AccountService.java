package com.pismo.banking.account.api;

import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.account.internal.model.Account;
import com.pismo.banking.common.exception.AccountNotFoundException;

/**
 * Service interface for managing bank accounts.
 * Provides functionality for creating, retrieving, and validating accounts.
 */
public interface AccountService {

    /**
     * Creates a new account based on the provided request details.
     *
     * @param accountRequest The request object containing details for the new account (e.g., document number).
     * @return An {@link AccountResponse} object representing the newly created account.
     * @throws AccountAlreadyExistsException if an account with the provided document number already exists.
     */
    AccountResponse createAccount(AccountRequest accountRequest);

    /**
     * Retrieves an existing account by its unique identifier.
     *
     * @param accountId The unique ID of the account to retrieve.
     * @return An {@link AccountResponse} object representing the found account.
     * @throws AccountNotFoundException if no account exists with the provided ID.
     */
    AccountResponse getAccountById(Long accountId);

    /**
     * Validates that an account exists for the given ID.
     * This method is typically used as a prerequisite check before performing
     * operations that require an active account (e.g., creating a transaction).
     *
     * @param accountId The unique ID of the account to validate.
     * @throws AccountNotFoundException if no account exists with the provided ID.
     */
    void validateAccountExists(Long accountId);

    Account findById(Long accountId);

    void updateAccount(Account account);
}
