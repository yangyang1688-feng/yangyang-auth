package com.yy.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// 1. 单例模式
@Component
public class ClientRegistrarSingleton {
    
    // 静态成员变量
    private static ClientRegistrar staticRegistrar;
    
    @Resource
    private WebClient oauthWebClient;
    
    @PostConstruct
    public void init() {
        // 初始化静态实例
        staticRegistrar = new ClientRegistrar(oauthWebClient);
    }
    
    /**
     * 获取静态实例
     */
    public static ClientRegistrar getInstance() {
        if (staticRegistrar == null) {
            throw new IllegalStateException("ClientRegistrar not initialized. Please check @PostConstruct.");
        }
        return staticRegistrar;
    }
}