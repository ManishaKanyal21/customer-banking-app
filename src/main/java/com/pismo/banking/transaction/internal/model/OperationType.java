package com.pismo.banking.transaction.internal.model;

import com.pismo.banking.transaction.internal.exception.InvalidOperationTypeException;

import java.util.stream.Stream;

/**
 * Defines the available types of financial operations (transactions) supported by the system.
 * Each type has an associated unique ID and determines whether the transaction
 * is recorded as a debit (negative amount) or a credit (positive amount).
 */
public enum OperationType {
    /**
     * Represents a standard purchase transaction. Recorded as a debt (negative amount).
     * ID: 1
     */
    PURCHASE(1),
    /**
     * Represents a installment purchase. Recorded as a debt (negative amount).
     * ID: 2
     */
    INSTALLMENT_PURCHASE(2),
    /**
     * Represents a withdrawal. Recorded as a debt (negative amount).
     * ID: 3
     */
    WITHDRAWAL(3),
    /**
     * Represents a payment made into an account. Recorded as a credit (positive amount).
     * ID: 4
     */
    PAYMENT(4);

    public final int id;

    /**
     * Returns the unique identifier associated with this operation type.
     *
     * @return The integer ID (1-4).
     */
    public int getId() {
        return id;
    }

    OperationType(int id) {
        this.id = id;
    }

    /**
     * Retrieves an {@link OperationType} enum constant corresponding to the given ID.
     *
     * @param id The integer ID of the operation type.
     * @return The matching OperationType enum constant.
     * @throws InvalidOperationTypeException if the provided ID does not match any existing operation type.
     * @see #getId()
     */
    public static OperationType fromId(int id) {
        return Stream.of(OperationType.values()).filter(value -> value.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidOperationTypeException(id));
    }
}
