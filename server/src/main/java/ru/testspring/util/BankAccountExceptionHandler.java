package ru.testspring.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.testspring.exceptions.BankAccountNotFoundException;
import ru.testspring.exceptions.IncorrectBalanceException;
import ru.testspring.exceptions.dto.BankAccountExceptionResponseDto;

@ControllerAdvice
public class BankAccountExceptionHandler {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<BankAccountExceptionResponseDto> handleBalanceNotFoundException(BankAccountNotFoundException e) {
        BankAccountExceptionResponseDto response =
                new BankAccountExceptionResponseDto(e.getBankAccountId(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectBalanceException.class)
    public ResponseEntity<BankAccountExceptionResponseDto> handleBalanceNotFoundException(IncorrectBalanceException e) {
        BankAccountExceptionResponseDto response =
                new BankAccountExceptionResponseDto(e.getBankAccountId(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
