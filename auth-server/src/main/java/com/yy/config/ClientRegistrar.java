package com.yy.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yy.vo.in.ClientRegInVO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
@Slf4j
public class ClientRegistrar {
	private final WebClient webClient;

	public ClientRegistrar(WebClient webClient) {
		this.webClient = webClient;
	}

	public record ClientRegistrationRequest(
			@JsonProperty("client_id") String clientId,
			@JsonProperty("client_name") String clientName,
			@JsonProperty("grant_types") List<String> grantTypes,
			@JsonProperty("redirect_uris") List<String> redirectUris,
			@JsonProperty("logo_uri") String logoUri,
			List<String> contacts,
			String scope,
			@JsonProperty("token_endpoint_auth_method") String tokenEndpointAuthMethod,
			@JsonProperty("token_endpoint_auth_methods") List<String> clientAuthenticationMethods,
			@JsonProperty("client_settings")Map<String,Object> clientSettings,
			@JsonProperty("settings.client.require-proof-key") boolean requireProofKey,
			@JsonProperty("settings.client.require-authorization-consent") boolean requireAuthorizationConsent) {
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
			String scope,
			@JsonProperty("token_endpoint_auth_method") String tokenEndpointAuthMethod,
			@JsonProperty("token_endpoint_auth_methods") List<String> clientAuthenticationMethods,
			@JsonProperty("client_settings")Map<String,Object> clientSettings,
			@JsonProperty("settings.client.require-proof-key") boolean requireProofKey,
			@JsonProperty("settings.client.require-authorization-consent") boolean requireAuthorizationConsent) {
	}

	public void exampleRegistration(String initialAccessToken) {
		Map<String, Object> map = Maps.newHashMap("require-proof-key", false);
		map.put("require-authorization-consent", false);
		ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(
				null,
				"client-1",
				List.of(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()),
				List.of("http://localhost:8080/login/oauth2/code/test-client", "https://www.baidu.com"),
				"https://www.baidu.com",
				List.of("contact-1", "contact-2"),
				"openid email profile",
				"client_secret_basic", // 主要认证方法
				List.of("client_secret_basic", "client_secret_post"),
				map,false,true

		);

		ClientRegistrationResponse clientRegistrationResponse =
				registerClient(initialAccessToken, clientRegistrationRequest);
		System.out.println("客户端密钥为："+clientRegistrationResponse.clientSecret);
		assert (clientRegistrationResponse.clientName().contentEquals("client-1"));
		assert (!Objects.isNull(clientRegistrationResponse.clientSecret()));
//		assert (clientRegistrationResponse.scope().contentEquals("openid email profile"));
		assert (clientRegistrationResponse.grantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		assert (clientRegistrationResponse.redirectUris().contains("http://localhost:8080/login/oauth2/code/test-client"));
		assert (clientRegistrationResponse.redirectUris().contains("https://www.baidu.com"));
		assert (!clientRegistrationResponse.registrationAccessToken().isEmpty());
		assert (!clientRegistrationResponse.registrationClientUri().isEmpty());
		assert (clientRegistrationResponse.logoUri().contentEquals("https://www.baidu.com"));
		assert (clientRegistrationResponse.contacts().size() == 2);
		assert (clientRegistrationResponse.contacts().contains("contact-1"));
		assert (clientRegistrationResponse.contacts().contains("contact-2"));

		String registrationAccessToken = clientRegistrationResponse.registrationAccessToken();
		String registrationClientUri = clientRegistrationResponse.registrationClientUri();

		ClientRegistrationResponse retrievedClient = retrieveClient(registrationAccessToken, registrationClientUri);

		assert (retrievedClient.clientName().contentEquals("client-1"));
		assert (!Objects.isNull(retrievedClient.clientId()));
		assert (!Objects.isNull(retrievedClient.clientSecret()));
//		assert (retrievedClient.scope().contentEquals("openid email profile"));
		assert (retrievedClient.grantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		assert (retrievedClient.redirectUris().contains("http://localhost:8080/login/oauth2/code/test-client"));
		assert (retrievedClient.redirectUris().contains("https://www.baidu.com"));
		assert (retrievedClient.logoUri().contentEquals("https://www.baidu.com"));
		assert (retrievedClient.contacts().size() == 2);
		assert (retrievedClient.contacts().contains("contact-1"));
		assert (retrievedClient.contacts().contains("contact-2"));
		assert (Objects.isNull(retrievedClient.registrationAccessToken()));
		assert (!retrievedClient.registrationClientUri().isEmpty());
	}

	public ClientRegistrationResponse registerClientServe(String initialAccessToken, ClientRegInVO vo){
		log.info("动态注册客户端请求参数为：{}", JSONUtil.toJsonStr(vo));
		ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(
				vo.getClientId(),
				vo.getClientName(),
				vo.getGrantTypes(),
				vo.getRedirectUris(),
				vo.getLogoUri(),
				vo.getContacts(),
				vo.getScope(),
				vo.getTokenEndpointAuthMethod(), // 主要认证方法
				vo.getClientAuthenticationMethods(),
				null, vo.isRequireProofKey(), vo.isRequireAuthorizationConsent()

		);
		ClientRegistrationResponse res = registerClient(initialAccessToken, clientRegistrationRequest);
		log.info("客户端注册成功，clientId ={} clientSecret ={}", res.clientId(), res.clientSecret());
		return res;
	}

	public ClientRegistrationResponse registerClient(String initialAccessToken, ClientRegistrationRequest request) {
        try {
            return this.webClient
                    .post()
                    .uri("/connect/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer "+initialAccessToken)
//				.body(Mono.just(request), ClientRegistrationRequest.class)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        // 将HTTP错误信息转换为异常消息
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "HTTP " + response.statusCode() + " - " + errorBody)));
                    })
                    .bodyToMono(ClientRegistrationResponse.class)
                    .block();
        } catch (Exception e) {
			System.out.println("请求失败："+e.getMessage());
            throw e;
        }
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