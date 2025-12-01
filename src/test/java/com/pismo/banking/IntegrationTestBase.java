package com.pismo.banking;


import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestBase {

    @LocalServerPort
    protected int port;

    protected Long createTestAccount(final String documentNumber) {
        RestAssured.port = port;
        final Long accountId = given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .body(getAccountPayload(documentNumber))
                .when()
                .post("/accounts")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("account_id");

        assertThat(accountId)
                .as("Account id is null.")
                .isNotNull().isPositive();
        return accountId;
    }

    protected static String getAccountPayload(final String documentNumber) {
        return """
                {
                "document_number" : "%s"
                }
                """.formatted(documentNumber);
    }

    /**
     * Generates an 11-digit random long number String
     */
    protected String generateUniqueDocNumber() {
        long min = 10000000000L; // Smallest 11-digit number
        long max = 99999999999L; // Largest 11-digit number
        return String.valueOf(RandomUtils.nextLong(min, max));
    }
}
