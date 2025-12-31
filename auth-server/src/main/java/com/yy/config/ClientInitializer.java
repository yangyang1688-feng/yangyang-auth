package com.yy.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
/**
 * 授权服务器初始化客户端配置
 * @ClassName: ClientInitializer
 * @author: yangfeng
 * @date: 2025/12/23 11:07
 * @version: 1.0.0
 */
@Component
public class ClientInitializer {

    @Bean
    public ApplicationRunner configureClients(RegisteredClientRepository clientRepository) {
        return args -> {
            if (clientRepository.findByClientId("test-client") == null) {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("test-client")
                    .clientSecret("{noop}test-secret")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:8080/login/oauth2/code/test-client")
                    .scope(OidcScopes.OPENID)
                    .scope(OidcScopes.PROFILE)
                    .scope("read")
                    .scope("write")
                    .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                    .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(2))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build())
                    .build();
                clientRepository.save(client);
            }
            //配置一个动态注册客户端的注册商
            if(clientRepository.findByClientId("registrar-client") == null){
                RegisteredClient registrarClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("registrar-client")
                        .clientSecret("{noop}secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        //这行代码指定该客户端可以使用 "客户端凭证"授权模式。这种模式的特点是客户端代表自己而非用户申请权限，适合执行管理类操作
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        //这两行是权限的精髓，他们呢为该客户端授予了动态创建和读取其他客户端配置的权限。没有这些权限。该客户端将无法调用动态注册接口
                        .scope("client.create")
                        .scope("client.read")
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofHours(2))
                                .refreshTokenTimeToLive(Duration.ofDays(7))
                                .build())
                        .build();
                clientRepository.save(registrarClient);
            }
        };
    }
}