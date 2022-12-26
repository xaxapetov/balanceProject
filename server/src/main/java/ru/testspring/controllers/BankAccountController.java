package ru.testspring.controllers;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.testspring.exceptions.BankAccountNotFoundException;
import ru.testspring.models.dto.ResponseBankAccountDTO;
import ru.testspring.services.BalanceService;

@RestController
@RequestMapping("api/balance")
@AllArgsConstructor
@Slf4j
public class BankAccountController {

    private final BalanceService BankAccountService;
    private final MetricRegistry metricRegistry;
    private final int MILLIS_TO_MINUTE_RATE = 60_000;
    private final String GET_BALANCE_METRIC = "GET_BALANCE";
    private final String CHANGE_BALANCE_METRIC = "CHANGE_BALANCE";

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBankAccountDTO> getBalance(@PathVariable("id") long id) {
        log.debug("in >> getBalance(id={}) BankAccountController", id);

        setMeterNameAndMark(GET_BALANCE_METRIC);

        ResponseBankAccountDTO balanceDTO = ResponseBankAccountDTO.builder()
                .id(id)
                .balance(BankAccountService.getBalance(id)
                        .orElseThrow(() -> new BankAccountNotFoundException("Balance by id not found", id)))
                .build();
        log.debug("getBalance(id={}) method BankAccountController >> out", id);
        return new ResponseEntity<>(balanceDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseBankAccountDTO> changeBalance(@PathVariable("id") long id, @RequestParam Long amount) {
        log.debug("in >> changeBalance(id={}) BankAccountController", id);

        setMeterNameAndMark(CHANGE_BALANCE_METRIC);

        BankAccountService.changeBalance(id, amount);
        log.debug("changeBalance(id={}) method BankAccountController >> out", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Async
    @Scheduled(fixedRate = MILLIS_TO_MINUTE_RATE)
    public void getMetrics(){
        Meter meterForGetBalance = metricRegistry.meter(MetricRegistry.name(GET_BALANCE_METRIC));
        Meter meterForChangeBalance = metricRegistry.meter(MetricRegistry.name(CHANGE_BALANCE_METRIC));
        log.info("number of getBalance method requests per minute : {}", meterForGetBalance.getCount());
        log.info("number of changeBalance method requests per minute : {}", meterForChangeBalance.getCount());
        metricRegistry.remove(GET_BALANCE_METRIC);
        metricRegistry.remove(CHANGE_BALANCE_METRIC);
    }

    private void setMeterNameAndMark(String name){
        Meter meter = metricRegistry.meter(MetricRegistry.name(name));
        meter.mark();
    }


}
