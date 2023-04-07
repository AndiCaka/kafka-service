package com.kafka.core.config;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${service.fuji}")
	String fujiUrl;
	@Value("${service.geonetwork}")
	String geonetworkUrl;
	@Value("${service.keycloak}")
	String keycloakUrl;
	@Value("${fuji.auth.username}")
	String fujiUsername;
	@Value("${fuji.auth.password}")
	String fujiPassword;

	@Bean
	WebClient keycloakClient() throws SSLException {

		return WebClient.builder().baseUrl(keycloakUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.clientConnector(new ReactorClientHttpConnector(httpClient())).build();
	}

	@Bean
	WebClient geonetworkClient() throws SSLException {

		return WebClient.builder().baseUrl(geonetworkUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.clientConnector(new ReactorClientHttpConnector(httpClient())).build();
	}

	@Bean
	WebClient fujiClient() throws SSLException {
		return WebClient.builder().baseUrl(fujiUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(header -> header.setBasicAuth(fujiUsername, fujiPassword))
				.clientConnector(new ReactorClientHttpConnector(httpClient())).build();
	}

	HttpClient httpClient() throws SSLException {
		SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();
		return HttpClient.create().secure(t -> t.sslContext(sslContext));
	}
}
