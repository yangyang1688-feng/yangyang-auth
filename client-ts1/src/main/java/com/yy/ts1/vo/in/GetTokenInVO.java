package com.yy.ts1.vo.in;

import lombok.Data;

/**
 * 获取token的请求参数
 * @ClassName: GetTokenInVO
 * @author: yangfeng
 * @date 2026/1/7 17:00
 * @version: 1.0.0
 */
@Data
public class GetTokenInVO {
    /**
     *
     */
    private String clientId;
    /**
     *
     */
    private String clientSecret;
    /**
     *
     */
    private String grantType = "authorization_code";
    /**
     *
     */
    private String code;
    /**
     *
     */
    private String redirectUri;

}
