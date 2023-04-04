package com.kafka.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulersConfig {

    @Value("${schedulers.geonet.threadPoolSize}")
    Integer threadPoolSize;
    @Value("${schedulers.geonet.taskQueueSize}")
    Integer taskQueueSize;

    @Bean
    public Scheduler geonetEventScheduler() {
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "fiar-assessments-pool");
    }

    @Bean
    public WebClient fujiEventScheduler() {
        WebClient webClient = WebClient.create("https://localhost:8888");
        return webClient;
    }

    @Bean
    public WebClient geonetworkEventScheduler() {
        WebClient webClient = WebClient.create("http://localhost:8080");
        return webClient;
    }

}
