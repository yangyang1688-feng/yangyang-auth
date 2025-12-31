package com.yy.config;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

public class ClientRegistrar {
	private final WebClient webClient;

	public ClientRegistrar(WebClient webClient) {
		this.webClient = webClient;
	}

	public record ClientRegistrationRequest(
			@JsonProperty("client_name") String clientName,
			@JsonProperty("grant_types") List<String> grantTypes,
			@JsonProperty("redirect_uris") List<String> redirectUris,
			@JsonProperty("logo_uri") String logoUri,
			List<String> contacts,
			String scope) {
	}

	public record ClientRegistrationResponse(
			@JsonProperty("registration_access_token") String registrationAccessToken,
			@JsonProperty("registration_client_uri") String registrationClientUri,
			@JsonProperty("client_name") String clientName,
			@JsonProperty("client_id") String clientId,
			@JsonProperty("client_secret") String clientSecret,
			@JsonProperty("grant_types") List<String> grantTypes,
			@JsonProperty("redirect_uris") List<String> redirectUris,
		 	@JsonProperty("logo_uri") String logoUri,
		 	List<String> contacts,
			String scope) {
	}

	public void exampleRegistration(String initialAccessToken) {
		ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(
				"client-1",
				List.of(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()),
				List.of("https://client.example.org/callback", "https://client.example.org/callback2"),
				"https://client.example.org/logo",
				List.of("contact-1", "contact-2"),
				"openid email profile"
		);

		ClientRegistrationResponse clientRegistrationResponse =
				registerClient(initialAccessToken, clientRegistrationRequest);

		assert (clientRegistrationResponse.clientName().contentEquals("client-1"));
		assert (!Objects.isNull(clientRegistrationResponse.clientSecret()));
		assert (clientRegistrationResponse.scope().contentEquals("openid profile email"));
		assert (clientRegistrationResponse.grantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		assert (clientRegistrationResponse.redirectUris().contains("https://client.example.org/callback"));
		assert (clientRegistrationResponse.redirectUris().contains("https://client.example.org/callback2"));
		assert (!clientRegistrationResponse.registrationAccessToken().isEmpty());
		assert (!clientRegistrationResponse.registrationClientUri().isEmpty());
		assert (clientRegistrationResponse.logoUri().contentEquals("https://client.example.org/logo"));
		assert (clientRegistrationResponse.contacts().size() == 2);
		assert (clientRegistrationResponse.contacts().contains("contact-1"));
		assert (clientRegistrationResponse.contacts().contains("contact-2"));

		String registrationAccessToken = clientRegistrationResponse.registrationAccessToken();
		String registrationClientUri = clientRegistrationResponse.registrationClientUri();

		ClientRegistrationResponse retrievedClient = retrieveClient(registrationAccessToken, registrationClientUri);

		assert (retrievedClient.clientName().contentEquals("client-1"));
		assert (!Objects.isNull(retrievedClient.clientId()));
		assert (!Objects.isNull(retrievedClient.clientSecret()));
		assert (retrievedClient.scope().contentEquals("openid profile email"));
		assert (retrievedClient.grantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		assert (retrievedClient.redirectUris().contains("https://client.example.org/callback"));
		assert (retrievedClient.redirectUris().contains("https://client.example.org/callback2"));
		assert (retrievedClient.logoUri().contentEquals("https://client.example.org/logo"));
		assert (retrievedClient.contacts().size() == 2);
		assert (retrievedClient.contacts().contains("contact-1"));
		assert (retrievedClient.contacts().contains("contact-2"));
		assert (Objects.isNull(retrievedClient.registrationAccessToken()));
		assert (!retrievedClient.registrationClientUri().isEmpty());
	}

	public ClientRegistrationResponse registerClient(String initialAccessToken, ClientRegistrationRequest request) {
		return this.webClient
				.post()
				.uri("/connect/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer "+initialAccessToken)
				.body(Mono.just(request), ClientRegistrationRequest.class)
				.retrieve()
				.bodyToMono(ClientRegistrationResponse.class)
				.block();
	}

	public ClientRegistrationResponse retrieveClient(String registrationAccessToken, String registrationClientUri) {
		return this.webClient
				.get()
				.uri(registrationClientUri)
				.header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(registrationAccessToken))
				.retrieve()
				.bodyToMono(ClientRegistrationResponse.class)
				.block();
	}

}