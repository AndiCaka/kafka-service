package com.kafka.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kafka.core.services.IGeoNetService;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class Schedule {

	@Autowired
	private IGeoNetService geoNetService;

	@Autowired
	WebClient geonetworkClient;
	@Scheduled(fixedRate = 3000) // 1:01:am
	public void geonetScheduler() {
	geoNetService.getDoiList();
//		geoNetService.process();

	}
}
