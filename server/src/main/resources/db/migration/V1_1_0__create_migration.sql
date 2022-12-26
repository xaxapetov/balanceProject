--H2
CREATE TABLE IF NOT EXISTS bank_account
(
    id    bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    balance bigint NOT NULL CHECK (balance >= 0)
)