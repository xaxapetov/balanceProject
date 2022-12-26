package ru.testspring.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testspring.repositories.BankAccountRepository;
import ru.testspring.services.BankAccountService;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountCacheUpdaterServiceImpl bankAccountCacheUpdaterService;

    @Override
    @Cacheable(value = "cacheBalance", key = "#id")
    @Transactional(readOnly = true)
    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public Optional<Long> getBalance(Long id) {
        log.debug("getBalance(id={}) BankAccountServiceImpl", id);
        return bankAccountRepository.getBalanceFromId(id);
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        bankAccountCacheUpdaterService.changeBalance(id,amount);
    }
}
