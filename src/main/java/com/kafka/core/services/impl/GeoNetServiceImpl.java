package com.kafka.core.services.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.common.exception.UnauthorizedException;
import com.kafka.core.config.Event;
import com.kafka.core.dto.FujiDTO;
import com.kafka.core.dto.GeoNetDTO;
import com.kafka.core.dto.KeycloakTokenDTO;
import com.kafka.core.services.IGeoNetService;

import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class GeoNetServiceImpl implements IGeoNetService {

	private static final String TOKEN_EXPIRED = "Token expired!";
	private final StreamBridge streamBridge;
	private final Scheduler geonetEventScheduler;
	private final WebClient fujiClient;
	private final WebClient geonetworkClient;
	private final WebClient keycloakClient;

	private String token;

	@Autowired
	public GeoNetServiceImpl(StreamBridge streamBridge, Scheduler geonetEventScheduler, WebClient fujiClient,
			WebClient geonetworkClient, WebClient keycloakClient) {
		super();
		this.streamBridge = streamBridge;
		this.geonetEventScheduler = geonetEventScheduler;
		this.fujiClient = fujiClient;
		this.geonetworkClient = geonetworkClient;
		this.keycloakClient = keycloakClient;
		this.token = setToken();
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

		try {
			Mono<List<GeoNetDTO>> response = geonetworkClient.get().accept(MediaType.APPLICATION_JSON)
					.headers(h -> h.setBearerAuth(token)).retrieve()
					.onStatus(httpStatus -> httpStatus.value() == 401,
							error -> Mono.error(new UnauthorizedException(TOKEN_EXPIRED)))
					.bodyToMono(new ParameterizedTypeReference<List<GeoNetDTO>>() {
					});

			List<GeoNetDTO> dois = response.block();
			if (dois != null) {
				return dois;
			} else {
				return Collections.emptyList();
			}
		} catch (UnauthorizedException e) {

			System.err.println(e.getMessage());
			this.token = setToken();
			return getDoiList();
		}
	}

	private String setToken() {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "password");
		formData.add("username", "kafka-service");
		formData.add("password", "password");
		formData.add("client_id", "geonetwork");
		formData.add("client_secret", "qzkZIINTppUBNumIOHOHhb8rMbJVb88I");

		JSONObject response = keycloakClient.post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData)).exchange().block().bodyToMono(JSONObject.class).block();

		return response.getAsString("access_token");
	}

}
