package com.pismo.banking.account.internal.repository;

import com.pismo.banking.account.internal.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Should save an account successfully and retrieve it by document number")
    void whenSaveAccount_thenFindsByDocumentNumber() {
        String docNumber = "12345678900";
        Account account = new Account(null,docNumber);

        Account savedAccount = accountRepository.save(account);

        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getAccountId()).isPositive();

        Optional<Account> foundAccount = accountRepository.findByDocumentNumber(docNumber);
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getDocumentNumber()).isEqualTo(docNumber);
    }

    @Test
    @DisplayName("Should fail to save if document number constraint is violated (Duplicate)")
    void whenSaveDuplicateDocumentNumber_thenThrowsException() {
        String docNumber = "55555555555";

        accountRepository.save(new Account(null,docNumber));

        Account duplicateAccount = new Account(null,docNumber);

        assertThrows(DataIntegrityViolationException.class, () -> {
            accountRepository.save(duplicateAccount);
        });
    }
}