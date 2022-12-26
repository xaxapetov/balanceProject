package ru.testspring.exceptions;

import lombok.Getter;

@Getter
public class IncorrectBalanceException extends IllegalArgumentException {
    private final Long bankAccountId;
    public IncorrectBalanceException(String message, Long bankAccountId){
        super(message);
        this.bankAccountId = bankAccountId;
    }
}
