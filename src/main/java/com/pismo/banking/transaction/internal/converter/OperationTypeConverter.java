package com.pismo.banking.transaction.internal.converter;

import com.pismo.banking.transaction.internal.model.OperationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static lombok.Lombok.checkNotNull;

/**
 * JPA Attribute Converter to map the OperationType Enum to an Integer database column (and vice-versa).
 * This converter ensures that the database values start at 1 (as defined in the enum's dbValue field)
 * rather than the default 0-based ordinal index used by @Enumerated(EnumType.ORDINAL).
 *
 * The autoApply = true setting means this converter is automatically used for all fields
 * of type OperationType within the application's entities.
 */
@Converter(autoApply = true)
public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {

    /**
     * Converts the Java Enum object into the database column value (Integer).
     *
     * Assumes the 'attribute' is never null because the entity field is annotated
     * with @Column(nullable = false), enforcing non-nullability at the database level.
     *
     * @param operationType The OperationType enum value from the Java entity.
     * @return The corresponding integer value to store in the database (1, 2, 3, or 4).
     */
    @Override
    public Integer convertToDatabaseColumn(final OperationType operationType) {
        return operationType.getId();
    }

    /**
     * Converts the database column value (Integer) back into the Java Enum object.
     *
     * We use a utility method to assert that the database value is not null, upholding the
     * @Column(nullable = false) constraint declared on the entity field.
     *
     * @param dbData The integer value loaded from the database column.
     * @return The corresponding OperationType enum value.
     * @throws IllegalArgumentException if the database value is null or unknown.
     */
    @Override
    public OperationType convertToEntityAttribute(final Integer dbData) {
        checkNotNull(dbData, "Operation type DB value cannot be null when converting to entity attribute.");
        return OperationType.fromId(dbData);
    }
}
