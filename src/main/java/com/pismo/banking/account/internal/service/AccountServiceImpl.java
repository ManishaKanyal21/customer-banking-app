package com.pismo.banking.account.internal.service;

import com.pismo.banking.account.api.AccountService;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.internal.exception.AccountAlreadyExistsException;
import com.pismo.banking.account.internal.model.Account;
import com.pismo.banking.common.exception.AccountNotFoundException;
import com.pismo.banking.account.internal.mapper.AccountMapper;
import com.pismo.banking.account.internal.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResponse createAccount(final AccountRequest accountRequest) {
        final String documentNumber = accountRequest.documentNumber();
        if(accountRepository.existsByDocumentNumber(documentNumber)){
            throw new AccountAlreadyExistsException(documentNumber);
        }

        final Account account = AccountMapper.toEntity(accountRequest);
        final Account savedAccount = accountRepository.save(account);
        return AccountMapper.toDto(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(final Long accountId) {
        return accountRepository.findById(accountId)
                .map(AccountMapper::toDto)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void validateAccountExists(final Long accountId) {
        if(!accountRepository.existsById(accountId)){
            throw new AccountNotFoundException(accountId);
        }
    }
}
