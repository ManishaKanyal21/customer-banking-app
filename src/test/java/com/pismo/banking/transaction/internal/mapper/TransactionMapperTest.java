package com.pismo.banking.transaction.internal.mapper;

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
    @DisplayName("Should map entity to DTO correctly with positive amount and all other fields")
    void testToDto_PositiveAmount() {
        Transaction entity = new Transaction(
                1L,
                101L,
                OperationType.PAYMENT,
                new BigDecimal("50.00"),
                LocalDateTime.now()
        );

        TransactionResponse dto = TransactionMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.transactionId()).isEqualTo(1L);
        assertThat(dto.accountId()).isEqualTo(101L);
        assertThat(dto.operationTypeId()).isEqualTo(4);
        assertThat(dto.amount()).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Should map entity to DTO correctly with negative amount and all other fields")
    void testToDto_NegativeAmount() {
        Transaction entity = new Transaction(
                2L,
                101L,
                OperationType.PURCHASE,
                new BigDecimal("-25.50"),
                LocalDateTime.now()
        );

        TransactionResponse dto = TransactionMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.transactionId()).isEqualTo(2L);
        assertThat(dto.operationTypeId()).isEqualTo(1);
        assertThat(dto.amount()).isEqualByComparingTo("-25.50");
    }

    @Test
    @DisplayName("Should return null when mapping a null entity to DTO")
    void testMapEntityToDto_HandlesNullInput() {
        TransactionResponse dto = TransactionMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Should map details to entity correctly, including setting current time")
    void testToEntity() {
        final Long accountId = 202L;
        final OperationType type = OperationType.WITHDRAWAL;
        final BigDecimal finalSignedAmount = new BigDecimal("-10.00");

        final Transaction entity = TransactionMapper.toEntity(accountId, type, finalSignedAmount);

        assertThat(entity).isNotNull();
        assertThat(entity.getAccountId()).isEqualTo(accountId);
        assertThat(entity.getOperationType()).isEqualTo(type);
        assertThat(entity.getAmount()).isEqualByComparingTo(finalSignedAmount);
        assertThat(entity.getEventDate()).isNotNull();
        assertThat(entity.getTransactionId()).isNull();
    }
}
