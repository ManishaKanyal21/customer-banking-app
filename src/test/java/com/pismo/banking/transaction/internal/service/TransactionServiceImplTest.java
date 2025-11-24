package com.pismo.banking.transaction.internal.service;

import com.pismo.banking.account.api.AccountService;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.model.OperationType;
import com.pismo.banking.transaction.internal.model.Transaction;
import com.pismo.banking.transaction.internal.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Service Unit Tests")
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("100.00");

    @Test
    @DisplayName("Should create transaction for purchase (OperationType 1) as a negative amount (Debit)")
    void testCreateTransactionForPurchaseAsNegative() {
        BigDecimal expectedFinalAmount = TEST_AMOUNT.negate();
        doNothing().when(accountService).validateAccountExists(TEST_ACCOUNT_ID);

        Transaction savedEntity = new Transaction(99L, TEST_ACCOUNT_ID, OperationType.PURCHASE, expectedFinalAmount, null);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedEntity);

        TransactionRequest transactionRequest = new TransactionRequest(TEST_ACCOUNT_ID, 1, TEST_AMOUNT);
        TransactionResponse result = transactionService.createTransaction(transactionRequest);

        assertThat(result.amount()).isEqualTo(expectedFinalAmount);
        assertThat(result.operationTypeId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should create transaction for Payment (OpType 4) as a positive amount (Credit)")
    void testCreateTransactionForPaymentAsPositive() {
        BigDecimal expectedFinalAmount = TEST_AMOUNT;
        doNothing().when(accountService).validateAccountExists(TEST_ACCOUNT_ID);

        Transaction savedEntity = new Transaction(99L, TEST_ACCOUNT_ID, OperationType.PAYMENT, expectedFinalAmount, null);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedEntity);

        TransactionRequest transactionRequest = new TransactionRequest(TEST_ACCOUNT_ID, 4, TEST_AMOUNT);
        TransactionResponse result = transactionService.createTransaction(transactionRequest);

        assertThat(result.amount()).isEqualTo(expectedFinalAmount);
        assertThat(result.operationTypeId()).isEqualTo(4);
        assertThat(result.accountId()).isEqualTo(TEST_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Should call account validation service before saving transaction")
    void testAccountServiceValidationCalled() {
        doNothing().when(accountService).validateAccountExists(TEST_ACCOUNT_ID);
        Transaction savedEntity = new Transaction(99L, TEST_ACCOUNT_ID, OperationType.PAYMENT, TEST_AMOUNT, null);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedEntity);

        TransactionRequest transactionRequest = new TransactionRequest(TEST_ACCOUNT_ID, 1, TEST_AMOUNT);
        transactionService.createTransaction(transactionRequest);

        verify(accountService, times(1)).validateAccountExists(TEST_ACCOUNT_ID);
    }
}
