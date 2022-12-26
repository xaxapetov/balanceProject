package ru.testspring.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.testspring.exceptions.BankAccountNotFoundException;
import ru.testspring.exceptions.IncorrectBalanceException;
import ru.testspring.models.BankAccount;
import ru.testspring.repositories.BankAccountRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BalanceServiceCacheUpdaterServiceImpl {

    private final BankAccountRepository bankAccountRepository;

    @CachePut(value = "cacheBalance", key = "#id")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public Optional<Long> changeBalance(Long id, Long amount) {
        log.debug("changeBalance(id={}) BankAccountCacheUpdaterServiceImpl", id);
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Balance by id not found", id));
        Long balance = bankAccount.getBalance();
        if ((balance + amount) < 0) {
            throw new IncorrectBalanceException("Balance is less or more then amount", id);
        }
        bankAccount.setBalance(balance + amount);
        bankAccountRepository.save(bankAccount);
        return Optional.of(bankAccount.getBalance());
    }
}

