package com.yy.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
/**
 * jwt 令牌配置
 * @ClassName: TokenConfig
 * @author: yangfeng
 * @date: 2025/12/23 11:05
 * @version: 1.0.0
 */
@Configuration
public class TokenConfig {
    // 使用固定的密钥对，确保测试一致性
    private static final KeyPair FIXED_TEST_KEY_PAIR = generateFixedKeyPair();

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        RSAPublicKey  publicKey = (RSAPublicKey) FIXED_TEST_KEY_PAIR.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) FIXED_TEST_KEY_PAIR.getPrivate();
        
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID("yangyang-auth")
            .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 生成一个固定的密钥对，避免每次测试都变化
     */
    private static KeyPair generateFixedKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            // 可以在这里指定一个固定的随机种子，使每次生成的密钥对完全相同
            // keyPairGenerator.initialize(2048, new SecureRandom("your-fixed-seed".getBytes()));
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }

}