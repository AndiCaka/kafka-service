package com.kafka.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
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

@Service
public class GeoNetServiceImpl implements IGeoNetService {

	private final StreamBridge streamBridge;
	private final Scheduler geonetEventScheduler;
	private final WebClient fujiClient;
	private final WebClient geonetworkClient;

	private String token = "eyJleHAiOjE2ODA3OTMyOTMsImlhdCI6MTY4MDc5Mjk5MywianRpIjoiOTE2ZDM4NjEtYzUwMC00YzhjLWE1ZDYtZDU2ZGY5N2U0ZTUxIiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5sZndibG4uZGV2LmxpbmZhLnNlcnZpY2VzL2F1dGgvcmVhbG1zL2dlb25ldHdvcmsiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOGU5MWI4ZjMtMTc0NS00NWIyLWFkYWQtODQwOTZlY2JhNmI1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZ2VvbmV0d29yayIsInNlc3Npb25fc3RhdGUiOiI1ZTkwYWJlYi03YzYwLTRiZTktODM4MS03ZGU3N2E5YTA1MDIiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWdlb25ldHdvcmsiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI1ZTkwYWJlYi03YzYwLTRiZTktODM4MS03ZGU3N2E5YTA1MDIiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImthZmthLXNlcnZpY2UifQ";
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
		// getDoiList().forEach(doi -> subscribe(doi));
		getDoiList();
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

		Mono<List<GeoNetDTO>> response = geonetworkClient.get()
				.accept(MediaType.APPLICATION_JSON).headers(h -> h.setBearerAuth(token)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<GeoNetDTO>>() {
				});

		List<GeoNetDTO> dois = response.block();
		if (dois != null)
			System.err.println("!NULL");
		else
			System.err.println("NULL");

		return null;// dois.stream().collect(Collectors.toList());
	}
}
