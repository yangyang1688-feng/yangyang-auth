package com.yy.ts1.controller;

import com.yy.ts1.vo.in.GetTokenInVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 第三方对接controller
 * @ClassName: ThirdController
 * @author: yangfeng
 * @date 2026/1/7 14:35
 * @version: 1.0.0
 */
@RestController
@RequestMapping
@Slf4j
public class ThirdController {
    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/getAuthorizationUrl")
    public String getAuthorizationUrl(String clientId, String scopes, String redirectUri, String codeChallenge) {
        return buildAuthorizationUrl(clientId, scopes,redirectUri, codeChallenge);
    }

    private String buildAuthorizationUrl(String clientId, String scopes,String redirectUri, String codeChallenge){
        StringBuilder sb = new StringBuilder("http://localhost:9000/oauth2/authorize?response_type=code");
        sb
            .append("&client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUri)
            .append("&scope=").append(scopes);
        if (codeChallenge != null) {
            sb.append("&code_challenge=").append(codeChallenge).append("&code_challenge_method=S256");
        }
        return sb.toString();
    }


    //https://www.baidu.com/?code=1Fx9i48yW4kMXhqmEamOgwQOHoWpQmK1HAG6UosIc-kO5hKb8JA2ZPnGKANBB2HYJqb-o0xl3OQvRU_R-2R8vt_J7l19kQHFuGogf_vh6F83QfktF_YzP41nsq48g-ly
    @PostMapping("/getToken")
    public String getToken(@RequestBody GetTokenInVO vo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", vo.getGrantType());
        params.add("code", vo.getCode());
        params.add("redirect_uri", vo.getRedirectUri());
        params.add("client_id", vo.getClientId());
        params.add("client_secret", vo.getClientSecret());

        // 发送POST请求到令牌端点
        String response = restTemplate.postForObject(
                "http://localhost:9000/oauth2/token",
                params,
                String.class
        );
        return response; // 响应中包含access_token, refresh_token等

    }
}
