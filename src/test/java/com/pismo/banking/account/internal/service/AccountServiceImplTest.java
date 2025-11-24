package com.pismo.banking.account.internal.service;

import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.account.internal.model.Account;
import com.pismo.banking.account.internal.repository.AccountRepository;
import com.pismo.banking.common.exception.AccountNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Unit Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final String VALID_DOC_NUMBER = "12345678900";
    private static final Long VALID_ACCOUNT_ID = 1L;
    private static final Long NON_EXISTENT_ACCOUNT_ID = 99L;


    @Test
    @DisplayName("Should successfully create a new account when document number is unique")
    void testCreateAccountSuccess() {
        when(accountRepository.existsByDocumentNumber(VALID_DOC_NUMBER)).thenReturn(false);
        Account savedEntity = new Account(VALID_ACCOUNT_ID, VALID_DOC_NUMBER);
        when(accountRepository.save(any(Account.class))).thenReturn(savedEntity);

        final AccountRequest accountRequest = new AccountRequest(VALID_DOC_NUMBER);

        AccountResponse result = accountService.createAccount(accountRequest);

        assertThat(result.accountId()).isEqualTo(VALID_ACCOUNT_ID);
        assertThat(result.documentNumber()).isEqualTo(VALID_DOC_NUMBER);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw AccountAlreadyExistsException if document number is not unique")
    void testCreateAccountThrowsForDuplicate() {

        when(accountRepository.existsByDocumentNumber(VALID_DOC_NUMBER)).thenReturn(true);

        final AccountRequest accountRequest = new AccountRequest(VALID_DOC_NUMBER);

        assertThatThrownBy(() -> accountService.createAccount(accountRequest))
                .isInstanceOf(AccountAlreadyExistsException.class)
                .hasMessageContaining("Account with document number " + VALID_DOC_NUMBER + " already exists");

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should retrieve an account if ID exists")
    void testGetAccountByIdSuccess() {
        Account foundEntity = new Account(VALID_ACCOUNT_ID, VALID_DOC_NUMBER);
        when(accountRepository.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(foundEntity));

        AccountResponse result = accountService.getAccountById(VALID_ACCOUNT_ID);

        assertThat(result.accountId()).isEqualTo(VALID_ACCOUNT_ID);
        verify(accountRepository, times(1)).findById(VALID_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException if account ID does not exist")
    void testGetAccountByIdThrowsForNonExistentId() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(NON_EXISTENT_ACCOUNT_ID))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account with id " + NON_EXISTENT_ACCOUNT_ID + " not found");
    }

    @Test
    @DisplayName("Should pass validation if account ID exists")
    void testValidateAccountExists() {
        when(accountRepository.existsById(VALID_ACCOUNT_ID)).thenReturn(true);

        accountService.validateAccountExists(VALID_ACCOUNT_ID);

        verify(accountRepository, times(1)).existsById(VALID_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException if validation fails")
    void testValidateAccountFails() {
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> accountService.validateAccountExists(NON_EXISTENT_ACCOUNT_ID))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account with id " + NON_EXISTENT_ACCOUNT_ID + " not found");
    }
}
