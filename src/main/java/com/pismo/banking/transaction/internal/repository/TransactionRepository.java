package com.pismo.banking.transaction.internal.repository;

import com.pismo.banking.transaction.internal.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface for managing {@link Transaction} entities.
 * Provides standard CRUD operations and custom query methods for transaction data access.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
