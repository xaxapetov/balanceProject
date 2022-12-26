package ru.testspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableRetry
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ServerApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServerApp.class, args);
    }
}
