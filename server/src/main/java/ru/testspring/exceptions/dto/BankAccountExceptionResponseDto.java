package ru.testspring.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class BankAccountExceptionResponseDto {
    private final Long bankAccountId;
    private final String message;
}
