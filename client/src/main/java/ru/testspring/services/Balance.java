package ru.testspring.services;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Balance implements Runnable {

    private final int readQuota;
    private final int writeQuota;
    private final List<Long> writeIdList;
    private final List<Long> readIdList;
    RestTemplate restTemplate = new RestTemplate();

    private static final String url = "http://localhost:8080/api/balance/";

    public Balance(int readQuota, int writeQuota, List<Long> writeIdList, List<Long> readIdList) {
        this.readQuota = readQuota;
        this.writeQuota = writeQuota;
        this.writeIdList = writeIdList;
        this.readIdList = readIdList;
    }

    @Override
    public void run() {
        while (true) {
            //вероятность вызова метода getBalance
            double readProbability = (double) readQuota / (double) (readQuota + writeQuota);

            if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                getBalance(randomFromList(readIdList));
            } else {
                changeBalance(randomFromList(writeIdList), 1L);
            }
        }
    }

    private void getBalance(long id) {
        try {
            restTemplate.getForEntity(url + id, String.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeBalance(long id, long amount) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url + id)
                .queryParam("amount", amount).build();
        try {
            restTemplate.postForObject(builder.toUriString(), null, String.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }

    }

    private long randomFromList(List<Long> idList) {
        int index = ThreadLocalRandom.current().nextInt(0, idList.size());
        return idList.get(index);
    }
}
