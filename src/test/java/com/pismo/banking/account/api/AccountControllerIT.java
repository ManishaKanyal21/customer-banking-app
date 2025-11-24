package com.pismo.banking.account.api;

import com.pismo.banking.IntegrationTestBase;
import com.pismo.banking.account.api.dto.AccountRequest;
import com.pismo.banking.account.api.dto.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account Controller E2E Tests")
public class AccountControllerIT extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("POST /accounts")
    class CreateAccountTests {

        @Test
        @DisplayName("Should return 201 CREATED for valid input and Location header")
        void shouldCreateAccountSuccessfully() {
            final String documentNumber = generateUniqueDocNumber();
            final AccountRequest requestPayload = new AccountRequest(documentNumber);

            final ResponseEntity<AccountResponse> response = restTemplate.postForEntity(
                    "/accounts",
                    requestPayload,
                    AccountResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isNotNull();

            final AccountResponse responseBody = response.getBody();
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.accountId()).isNotNull();
            assertThat(responseBody.documentNumber()).isEqualTo(documentNumber);

            final String locationHeader = response.getHeaders().getFirst(HttpHeaders.LOCATION);
            final Long accountId = responseBody.accountId();
            assertThat(locationHeader).endsWith(String.format("/accounts/%d", accountId));
        }

        @Test
        @DisplayName("Should return 409 CONFLICT for duplicate account")
        void shouldFailForDuplicateAccount() {
            final String docNumber = generateUniqueDocNumber();
            createTestAccount(docNumber);

            final AccountRequest requestPayload = new AccountRequest(docNumber);

            final ResponseEntity<String> response = restTemplate.postForEntity(
                    "/accounts",
                    requestPayload,
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

            assertThat(response.getBody())
                    .contains(String.format("Account with document number %s already exists", docNumber));
        }
    }

    @Nested
    @DisplayName("GET /accounts/{id}")
    class GetAccountTests {

        @Test
        @DisplayName("Should return 200 OK for existing account ID")
        void shouldGetExistingAccount() {
            final String documentNumber = generateUniqueDocNumber();
            final Long accountId = createTestAccount(documentNumber);

            final ResponseEntity<AccountResponse> response = restTemplate.getForEntity(
                    "/accounts/" + accountId,
                    AccountResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().accountId()).isEqualTo(accountId);
            assertThat(response.getBody().documentNumber()).isEqualTo(documentNumber);
        }

        @Test
        @DisplayName("Should return 404 Not Found for account ID for which no account exists")
        void shouldFailForNonExistingAccount() {
            final Long nonExistentId = 9999L;

            final ResponseEntity<String> response = restTemplate.getForEntity(
                    "/accounts/" + nonExistentId,
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

            assertThat(response.getBody())
                    .contains(String.format("Account with id %d not found", nonExistentId));
        }
    }

    @Nested
    @DisplayName("Validation Tests (POST /accounts)")
    class AccountValidationTests {

        @Test
        @DisplayName("Should return 400 BAD REQUEST for 10-digit document number")
        void shouldFailForShortDocumentNumber() {
            String invalidDoc = "1234567890";
            AccountRequest invalidPayload = new AccountRequest(invalidDoc);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "/accounts",
                    invalidPayload,
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody())
                    .contains("Validation failed:")
                    .contains("Document number must be 11 digits");
        }

        @Test
        @DisplayName("Should return 400 BAD REQUEST for document number with alphabets")
        void shouldFailForNonNumericDocumentNumber() {
            String invalidDoc = "abc45678900";
            AccountRequest invalidPayload = new AccountRequest(invalidDoc);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "/accounts",
                    invalidPayload,
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody())
                    .contains("Validation failed:")
                    .contains("Document number must be 11 digits");
        }

        @Test
        @DisplayName("Should return 400 BAD REQUEST for missing document number")
        void shouldFailForMissingDocumentNumber() {
            AccountRequest invalidPayload = new AccountRequest(null);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "/accounts",
                    invalidPayload,
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody())
                    .contains("Validation failed:")
                    .contains("Document number is required");
        }
    }
}
