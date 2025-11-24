package com.pismo.banking.transaction.internal.model;

import com.pismo.banking.transaction.internal.exception.InvalidOperationTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OperationType Enum Unit Tests")
class OperationTypeTest {

    @Test
    @DisplayName("Should return the correct ID for PURCHASE")
    void testGetId_Purchase() {
        assertThat(OperationType.PURCHASE.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return the correct ID for INSTALLMENT_PURCHASE")
    void testGetId_InstallmentPurchase() {
        assertThat(OperationType.INSTALLMENT_PURCHASE.getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return the correct enum for a valid existing ID")
    void testFromId_ValidIdReturnsEnum() {
        assertThat(OperationType.fromId(1)).isEqualTo(OperationType.PURCHASE);
        assertThat(OperationType.fromId(2)).isEqualTo(OperationType.INSTALLMENT_PURCHASE);
        assertThat(OperationType.fromId(3)).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(OperationType.fromId(4)).isEqualTo(OperationType.PAYMENT);
    }

    @Test
    @DisplayName("Should throw InvalidOperationTypeException if ID does not exist in the enum")
    void testFromId_NonExistentIdThrows() {
        int invalidId = 99;

        assertThatThrownBy(() -> OperationType.fromId(invalidId))
                .isInstanceOf(InvalidOperationTypeException.class)
                .hasMessageContaining(String.format("Operation type id %d is invalid.", invalidId));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ID is zero")
    void testFromId_ZeroIdThrowsCheckArgument() {
        int invalidId = 0;

        assertThatThrownBy(() -> OperationType.fromId(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Operation type id must be a positive integer.");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ID is negative")
    void testFromId_NegativeIdThrowsCheckArgument() {
        int invalidId = -1;

        assertThatThrownBy(() -> OperationType.fromId(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Operation type id must be a positive integer.");
    }
}
