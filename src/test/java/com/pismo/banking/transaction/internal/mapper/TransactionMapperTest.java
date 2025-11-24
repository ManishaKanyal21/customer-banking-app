package com.pismo.banking.transaction.internal.mapper;

import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import com.pismo.banking.transaction.internal.model.OperationType;
import com.pismo.banking.transaction.internal.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Transaction Mapper Unit Tests")
class TransactionMapperTest {

    @Test
    @DisplayName("Should correctly map all fields from Entity to Response DTO")
    void testMapEntityToDto_Success() {

        LocalDateTime now = LocalDateTime.now();

        Transaction entity = new Transaction(10L, 1L, OperationType.PURCHASE, new BigDecimal("-50.75"), now);

        TransactionResponse dto = TransactionMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.transactionId()).isEqualTo(10L);
        assertThat(dto.accountId()).isEqualTo(1L);
        assertThat(dto.operationTypeId()).isEqualTo(1);
        assertThat(dto.amount()).isEqualTo(new BigDecimal("-50.75"));
    }

    @Test
    @DisplayName("Should return null when mapping a null entity to DTO")
    void testMapEntityToDto_HandlesNullInput() {
        TransactionResponse dto = TransactionMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Should correctly map a purchase request (Op 1) and negate the amount")
    void testMapRequestToEntity_Purchase_NegatesAmount() {
        TransactionRequest request = new TransactionRequest(1L, 1, new BigDecimal("100.00"));
        Transaction entity = TransactionMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getAccountId()).isEqualTo(1L);
        assertThat(entity.getOperationType()).isEqualTo(OperationType.PURCHASE);
        assertThat(entity.getAmount()).isEqualTo(new BigDecimal("-100.00"));
        assertThat(entity.getEventDate()).isNotNull();
        assertThat(entity.getTransactionId()).isNull();
    }

    @Test
    @DisplayName("Should correctly map a payment request (Op 4) and keep amount positive")
    void testMapRequestToEntity_Payment_KeepsAmountPositive() {
        TransactionRequest request = new TransactionRequest(1L, 4, new BigDecimal("50.25"));

        Transaction entity = TransactionMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getOperationType()).isEqualTo(OperationType.PAYMENT);
        assertThat(entity.getAmount()).isEqualTo(new BigDecimal("50.25"));
    }

    @Test
    @DisplayName("Should return null when mapping a null request DTO to Entity")
    void testMapRequestToEntity_HandlesNullInput() {
        Transaction entity = TransactionMapper.toEntity(null);

        assertThat(entity).isNull();
    }
}
