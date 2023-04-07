package com.kafka.core.config;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageProcessorConfig {

	Logger logger = LoggerFactory.getLogger(MessageProcessorConfig.class);

	@Bean
	public Consumer<Event> process() {

		return event -> {
			logger.info("Processing message for doi: " + event.getGeoNetModel().getDoiURL());
			String assessment = event.assess().apply(event.getGeoNetModel().getDoiURL());
			logger.info("Made assessment: " + assessment);
			event.updateDOI().accept(event.getGeoNetModel().getId(), assessment);
		};
	}
}