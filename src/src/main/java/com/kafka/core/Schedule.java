package com.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kafka.core.config.MessageProcessorConfig;
import com.kafka.core.services.IGeoNetService;

@Configuration
@EnableScheduling
public class Schedule {

	Logger logger = LoggerFactory.getLogger(Schedule.class);

	@Autowired
	private IGeoNetService geoNetService;

	@Scheduled(fixedRate = 3000) // 1:01:am
	public void geonetScheduler() throws InterruptedException {
		logger.info("Starting scheduler.");
		geoNetService.process();

	}
}
