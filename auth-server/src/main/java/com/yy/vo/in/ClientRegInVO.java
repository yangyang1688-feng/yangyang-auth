package com.yy.vo.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 动态注册客户端请求参数
 * @ClassName: ClientRegInVO
 * @author: yangfeng
 * @date 2026/1/6 11:28
 * @version: 1.0.0
 */
@Data
public class ClientRegInVO {
    /**
     * 客户端id(可以手动指定，如果不指定则系统会自动生成一个)
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 授权类型
     */
    private List<String> grantTypes;
    /**
     * 回调地址
     */
    private List<String> redirectUris;
    /**
     * log地址
     */
    private String logoUri;
    /**
     * 额外内容
     */
    private List<String> contacts;
    /**
     * 申请权限
     */
    private String scope;
    /**
     * 客户端认证方法
     */
    private String tokenEndpointAuthMethod;
    /**
     * 客户端认证方法
     */
    private List<String> clientAuthenticationMethods;
    /**
     * 客户端配置
     */
    private Map<String,Object> clientSettings;
    /**
     * 一个关键的服务器端配置，用于在 OAuth 2.0/OpenID Connect
     * 授权码流程中强制要求客户端使用 PKCE，以显著提升安全性
     */
    private boolean requireProofKey;
    /**
     * 是一个关键的客户端配置项，它直接决定了在用户登录后，
     * 是否需要其显式地在一个授权同意页面上进行确认，之后授权服务器才会颁发授权码
     */
    private boolean requireAuthorizationConsent;
}
