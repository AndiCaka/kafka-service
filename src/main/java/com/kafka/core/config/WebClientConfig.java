package com.kafka.core.config;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${keycloak.auth.token-uri}")
	String keycloakTokenUri;
	@Value("${service.fuji}")
	String fujiUrl;
	@Value("${service.geonetwork}")
	String geonetworkUrl;
	@Value("${fuji.auth.username}")
	String fujiUsername;
	@Value("${fuji.auth.password}")
	String fujiPassword;

	@Bean
	WebClient keycloakClient() throws SSLException {

		return WebClient.builder().baseUrl(keycloakTokenUri)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();
	}

	@Bean
	WebClient geonetworkClient() throws SSLException {

		return WebClient.builder().baseUrl(geonetworkUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Bean
	WebClient fujiClient() {
		return WebClient.builder().baseUrl(fujiUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(header -> header.setBasicAuth(fujiUsername, fujiPassword)).build();
	}
}
