package com.kafka.core;

import com.kafka.core.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@EnableAsync
@Component
public class Schedule {

    @Autowired
    WebClient fujiEventScheduler;
    @Async
    @Scheduled(fixedRate = 3000) //1:01:am
    public void geonet() throws InterruptedException {
        fujiEventScheduler.post().uri("/evaulate");
    }
}
