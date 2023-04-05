package com.kafka.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static String userName = "marvel";
    private static String password = "wonderwoman";

    @Bean
    public WebClient fujiEventScheduler() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://localhost:8888")
                .defaultHeaders(header -> header.setBasicAuth(userName, password))
                .build();

        return webClient;
    }

    @Bean
    public WebClient geonetworkEventScheduler() {
        WebClient webClient = WebClient.create("http://localhost:8080");
        return webClient;
    }
}
