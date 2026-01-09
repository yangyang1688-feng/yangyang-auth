package com.yy.ts1.config;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 1. 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnTotal(200)                     // 最大连接数
                        .setMaxConnPerRoute(20)                   // 每个路由最大连接数
                        .build();

        // 2. 创建 RequestConfig (HttpClient 5.x 使用新的API)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5, TimeUnit.SECONDS)      // 连接超时
                .setResponseTimeout(10, TimeUnit.SECONDS)    // 响应超时（相当于读取超时）
                .setConnectionRequestTimeout(2, TimeUnit.SECONDS) // 获取连接超时
                .build();

        // 3. 创建 HttpClient
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()                   // 清理过期连接
                .evictIdleConnections(TimeValue.of(Duration.ofSeconds(30))) // 清理空闲连接
                .build();

        // 4. 创建 Spring 工厂
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}