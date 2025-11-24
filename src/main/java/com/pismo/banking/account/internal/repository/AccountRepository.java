package com.pismo.banking.account.internal.repository;

import com.pismo.banking.account.internal.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByDocumentNumber(String documentNumber);
}
