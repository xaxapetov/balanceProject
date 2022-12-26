package ru.testspring.exceptions;

import lombok.Getter;

@Getter
public class BankAccountNotFoundException extends NullPointerException {
    private final Long bankAccountId;
    public BankAccountNotFoundException(String message, Long bankAccountId){
        super(message);
        this.bankAccountId = bankAccountId;
    }

}
