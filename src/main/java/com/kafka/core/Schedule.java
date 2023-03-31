package com.kafka.core;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@EnableAsync
@Component
public class Schedule {
    @Async
    @Scheduled(fixedRate = 3000) //1:01:am
    public void geonet() throws InterruptedException {
        System.err.println(
                "Fixed rate task async - " + System.currentTimeMillis() / 1000);
    }
}
