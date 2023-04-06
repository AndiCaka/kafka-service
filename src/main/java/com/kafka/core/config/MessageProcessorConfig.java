package com.kafka.core.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageProcessorConfig {

	@Bean
	public Consumer<Event> process() {

		return event -> {
			String assessment = event.assess().apply(event.getGeoNetModel().getDoiURL());
			System.err.println(assessment);
			//event.updateDOI().accept(event.getGeoNetModel().getId(), assessment);
		};
	}
}