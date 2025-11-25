package com.pismo.banking.account.internal.mapper;

import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import com.pismo.banking.account.internal.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account Mapper Unit Tests")
class AccountMapperTest {

    @Test
    @DisplayName("Should correctly map Account Entity to AccountResponse DTO")
    void testMapEntityToDto() {
        Account entity = new Account(1L, "12345678900");

        AccountResponse dto = AccountMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.accountId()).isEqualTo(entity.getAccountId());
        assertThat(dto.documentNumber()).isEqualTo(entity.getDocumentNumber());
    }

    @Test
    @DisplayName("Should handle null entity mapping to DTO")
    void testMapEntityToDtoHandlesNull() {
        assertThat(AccountMapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Should correctly map AccountRequest DTO to Account Entity")
    void testMapRequestDtoToEntity() {
        AccountRequest request = new AccountRequest("12345678900");
        Account entity = AccountMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getAccountId()).isNull();
        assertThat(entity.getDocumentNumber()).isEqualTo(request.documentNumber());
    }
}