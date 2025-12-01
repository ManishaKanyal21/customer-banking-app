-- Accounts Table

ALTER TABLE accounts
ADD COLUMN balance NUMERIC(12, 2) NOT NULL;

ALTER TABLE accounts
ADD COLUMN credit_limit NUMERIC(12, 2) NOT NULL;


