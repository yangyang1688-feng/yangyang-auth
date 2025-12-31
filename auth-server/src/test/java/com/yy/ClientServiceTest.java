package com.yy;

import com.yy.config.ClientRegistrar;
import com.yy.dto.TokenDTO;
import io.gitee.loulan_yxq.owner.http.HttpMethod;
import io.gitee.loulan_yxq.owner.http.HttpTool;
import io.gitee.loulan_yxq.owner.json.tool.JsonTool;
import jakarta.annotation.Resource;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 客户端测试类
 * @ClassName: ClientServiceTest
 * @author: yangfeng
 * @date 2025/12/31 15:09
 * @version: 1.0.0
 */
@SpringBootTest
public class ClientServiceTest {
    @Resource
    private WebClient oauthWebClient;
    @Test
    void testDynamicRegister(){
        ClientRegistrar registrar = new ClientRegistrar(oauthWebClient);
        //获取token
        Map<String, String> headMap = new LinkedHashMap<>(2);
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        headMap.put("Authorization","Basic cmVnaXN0cmFyLWNsaWVudDpzZWNyZXQ=");

        Map<String, String> paramMap = new LinkedHashMap<>(3);
        paramMap.put("Content-Type","application/x-www-form-urlencoded");
        paramMap.put("grant_type", "client_credentials");
        paramMap.put("scope", "client.create");
        String post = HttpTool.request("http://localhost:9000/oauth2/token", HttpMethod.POST, StandardCharsets.UTF_8,headMap, paramMap, JsonTool.toJson(paramMap));
        TokenDTO tokenDTO = JsonTool.parseObj(post, TokenDTO.class);
        registrar.exampleRegistration(tokenDTO.getAccess_token());
    }
}
