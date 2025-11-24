package com.pismo.banking.transaction.internal.service;

import com.pismo.banking.account.api.AccountService;
import com.pismo.banking.transaction.api.TransactionService;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.mapper.TransactionMapper;
import com.pismo.banking.transaction.internal.model.Transaction;
import com.pismo.banking.transaction.internal.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(final TransactionRepository transactionRepository,
                                  final AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public TransactionResponse createTransaction(final TransactionRequest transactionRequest) {
        accountService.validateAccountExists(transactionRequest.accountId());
        final Transaction transaction = TransactionMapper.toEntity(transactionRequest);
        final Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toDto(savedTransaction);
    }
}
