package com.pismo.banking.transaction.internal.model;

import com.pismo.banking.transaction.internal.exception.InvalidOperationTypeException;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

public enum OperationType {
    PURCHASE(1),
    INSTALLMENT_PURCHASE(2),
    WITHDRAWAL(3),
    PAYMENT(4);

    public final int id;

    public int getId() {
        return id;
    }

    OperationType(int id) {
        this.id = id;
    }

    public static OperationType fromId(int id) {
        checkArgument(id > 0, "Operation type id must be a positive integer.");
        return Stream.of(OperationType.values()).filter(value -> value.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidOperationTypeException(id));
    }
}
