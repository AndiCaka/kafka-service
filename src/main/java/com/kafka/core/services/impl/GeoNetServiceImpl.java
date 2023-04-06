package com.kafka.core.services.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.core.config.Event;
import com.kafka.core.dto.FujiDTO;
import com.kafka.core.dto.GeoNetDTO;
import com.kafka.core.services.IGeoNetService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.netty.http.client.HttpClientRequest;

@Service
public class GeoNetServiceImpl implements IGeoNetService {

	private final StreamBridge streamBridge;
	private final Scheduler geonetEventScheduler;
	private final WebClient fujiClient;
	private final WebClient geonetworkClient;

	@Autowired
	public GeoNetServiceImpl(StreamBridge streamBridge, Scheduler geonetEventScheduler, WebClient fujiClient,
			WebClient geonetworkClient) {
		super();
		this.streamBridge = streamBridge;
		this.geonetEventScheduler = geonetEventScheduler;
		this.fujiClient = fujiClient;
		this.geonetworkClient = geonetworkClient;
	}

	@Override
	public void process() {
		getDoiList().forEach(doi -> subscribe(doi));
	}

	private Mono<GeoNetDTO> subscribe(GeoNetDTO geoNetDTO) {

		return Mono.fromCallable(() -> {
			sendMessage("process-out-0", new Event(geoNetDTO, doiURL -> assessDOI(doiURL),
					(doiId, assessment) -> updateDOI(doiId, assessment)));
			return geoNetDTO;
		}).subscribeOn(geonetEventScheduler);
	}

	public String assessDOI(String doi) {
		try {
			String fujiDTO = new ObjectMapper().writeValueAsString(new FujiDTO("", "", doi, true, true));
			return fujiClient.post().body(Mono.just(fujiDTO), String.class).retrieve().bodyToMono(String.class).block();
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	private void updateDOI(int id, String assessment) {
		// TODO: call geonet update endpoint
		geonetworkClient.put();
	}

	private void sendMessage(String bindingName, Event event) {
		Message message = MessageBuilder.withPayload(event).build();
		streamBridge.send(bindingName, message);
	}

	private List<GeoNetDTO> getDoiList() {

		Mono<List<GeoNetDTO>> response = geonetworkClient.get().uri("/metadatadoi/fairassesment")
				.accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<GeoNetDTO>>() {
				});

		List<GeoNetDTO> dois = response.block();
		return dois.stream().collect(Collectors.toList());
	}
}
