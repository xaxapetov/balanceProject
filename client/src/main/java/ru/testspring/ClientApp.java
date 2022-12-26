package ru.testspring;

import org.springframework.web.client.RestTemplate;
import ru.testspring.services.Balance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientApp {
//        threadCount - количество клиентских потоков (>= 1)
//        readQuota   - доля запросов getBalance (>= 0)
//        writeQuota  - доля запросов changeBalance (>= 0)
//        readIdList  - список идентификаторов для getBalance
//        writeIdList - список идентификаторов для changeBalance
    static final int THREAD_COUNT = 4;
    static final int READ_QUOTA = 10;
    static final int WRITE_QUOTA = 15;
    static final List<Long> READ_ID_LIST = Collections.unmodifiableList(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));
    static final List<Long> WRITE_ID_LIST = Collections.unmodifiableList(READ_ID_LIST);


    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        Balance balance = new Balance(READ_QUOTA, WRITE_QUOTA, WRITE_ID_LIST, READ_ID_LIST);


        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(new Balance(READ_QUOTA, WRITE_QUOTA, WRITE_ID_LIST, READ_ID_LIST));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }
}
