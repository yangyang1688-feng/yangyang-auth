package com.yy;

import com.yy.config.ClientRegistrar;
import com.yy.dto.TokenDTO;
import com.yy.tools.JsonParserTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.hutool.http.HttpRequest;

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
    void testDynamicRegister() throws Exception {
        ClientRegistrar registrar = new ClientRegistrar(oauthWebClient);
        //获取token
        Map<String, String> headMap = new LinkedHashMap<>(2);
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        headMap.put("Authorization","Basic cmVnaXN0cmFyLWNsaWVudDpzZWNyZXQ=");

        // 1. 准备表单参数（与图片中完全一致）
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        formParams.put("scope", "client.create");

        // 2. 发送POST请求
        String response = HttpRequest.post("http://localhost:9000/oauth2/token")
                .headerMap(headMap,false)
                .form(formParams)  // 关键：以表单形式传递参数
                .execute()
                .body();
        System.out.println(response);
        TokenDTO tokenDTO = JsonParserTool.parseToEntity(response,TokenDTO.class);
        registrar.exampleRegistration(tokenDTO.getAccess_token());

    }


}
