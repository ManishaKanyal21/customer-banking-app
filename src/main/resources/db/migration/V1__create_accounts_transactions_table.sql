-- Accounts Table
CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_number VARCHAR(11) NOT NULL UNIQUE
);

-- Transactions Table
CREATE TABLE transactions (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    operation_type INTEGER NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

