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

	@Value("${service.fuji}")
	private String fujiUrl;
	@Value("${service.geonetwork}")
	private String geonetworkUrl;
	@Value("${fuji.auth.username}")
	private String username;
	@Value("${fuji.auth.password}")
	private String password;
	@Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
	String tokenUri;
	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	String clientId;
	@Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
	String clientSecret;

	/*
	 * @Bean("cr") ReactiveClientRegistrationRepository getRegistration(
	 * 
	 * @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}") String
	 * tokenUri,
	 * 
	 * @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	 * String clientId,
	 * 
	 * @Value(
	 * "${spring.security.oauth2.client.registration.keycloak.client-secret}")
	 * String clientSecret) { ClientRegistration registration =
	 * ClientRegistration.withRegistrationId("keycloak").tokenUri(tokenUri)
	 * .clientId(clientId).clientSecret(clientSecret)
	 * .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build();
	 * return new InMemoryReactiveClientRegistrationRepository(registration); }
	 */

	/*
	 * @Bean WebClient geonetworkClient(ReactiveClientRegistrationRepository
	 * clientRegistrations) throws SSLException {
	 * ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new
	 * ServerOAuth2AuthorizedClientExchangeFilterFunction( clientRegistrations, new
	 * UnAuthenticatedServerOAuth2AuthorizedClientRepository());
	 * 
	 * oauth.setDefaultClientRegistrationId("keycloak"); return
	 * WebClient.builder().baseUrl(geonetworkUrl)
	 * .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	 * .defaultHeader(HttpHeaders.ACCEPT,
	 * MediaType.APPLICATION_JSON_VALUE).filter(oauth).build(); }
	 */

	@Bean
	WebClient geonetworkClient() throws SSLException {

		return WebClient.builder().baseUrl(geonetworkUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Bean
	public WebClient fujiClient() {
		return WebClient.builder().baseUrl(fujiUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(header -> header.setBasicAuth(username, password)).build();
	}
}
