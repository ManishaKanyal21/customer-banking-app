package com.pismo.banking.transaction.internal.service;

import com.pismo.banking.account.api.AccountService;
import com.pismo.banking.account.internal.model.Account;
import com.pismo.banking.common.exception.InSufficientLimitException;
import com.pismo.banking.transaction.api.TransactionService;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.mapper.TransactionMapper;
import com.pismo.banking.transaction.internal.model.OperationType;
import com.pismo.banking.transaction.internal.model.Transaction;
import com.pismo.banking.transaction.internal.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Concrete implementation of the {@link TransactionService} interface.
 * Orchestrates the creation and processing of transactions, applying core business rules.
 *
 * <p>Uses Spring's {@code @Service} stereotype and manages transactions declaratively
 * via {@code @Transactional}. It depends on {@link AccountService} for account validation.</p>
 */
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

    /**
     * @inheritDoc
     * <p>This implementation performs the following steps:</p>
     * <ol>
     *   <li>Resolves the {@link OperationType} from the request ID.</li>
     *   <li>Validates the target account exists using {@link AccountService#validateAccountExists(Long)}.</li>
     *   <li>Applies the business rule to correctly sign the transaction amount (positive for credits, negative for debits).</li>
     *   <li>Persists the validated {@link Transaction} entity using the repository.</li>
     * </ol>
     */
    @Override
    public TransactionResponse createTransaction(final TransactionRequest transactionRequest) {
        final OperationType operationType = OperationType.fromId(transactionRequest.operationTypeId());

        final Account account = accountService.findById(transactionRequest.accountId());
        final BigDecimal balance = account.getBalance();
        final BigDecimal limit = account.getLimit();
        final BigDecimal transactionAmount = transactionRequest.amount();
        final boolean isDebit = operationType != OperationType.PAYMENT;

        final BigDecimal finalTransactionAmount = isDebit ? transactionAmount.negate() : transactionAmount;
        final BigDecimal newBalance = finalTransactionAmount.add(balance);

        final BigDecimal availableLimit = newBalance.add(limit);

        if(availableLimit.compareTo(BigDecimal.ZERO)< 0){
          throw new InSufficientLimitException("The Limit has exceeded");
        }

        updateAccountWithNewBalance(account, newBalance);

        final Transaction transaction = TransactionMapper.toEntity(
                transactionRequest.accountId(),
                operationType,
                finalTransactionAmount
        );
        final Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toDto(savedTransaction);
    }

    private void updateAccountWithNewBalance(final Account account, final BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountService.updateAccount(account);
    }
}
