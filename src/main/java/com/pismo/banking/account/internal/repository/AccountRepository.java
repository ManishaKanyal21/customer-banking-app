package com.pismo.banking.account.internal.repository;

import com.pismo.banking.account.internal.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for managing {@link Account} entities.
 * Provides standard CRUD operations and custom query methods for account data access.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Checks if an account exists with the given document number.
     *
     * @param documentNumber The document number to check existence for.
     * @return true if an account exists, false otherwise.
     */
    boolean existsByDocumentNumber(String documentNumber);

    /**
     * Finds an account by its unique document number.
     *
     * @param documentNumber The 11-digit document number identifying the account owner.
     * @return An {@link Optional} containing the found account, or empty if no account exists with that document number.
     */
    Optional<Account> findByDocumentNumber(String documentNumber);
}
