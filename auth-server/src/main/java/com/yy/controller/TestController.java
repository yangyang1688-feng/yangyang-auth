package com.yy.controller;

import cn.hutool.http.HttpRequest;
import com.yy.config.ClientRegistrar;
import com.yy.config.ClientRegistrarSingleton;
import com.yy.dto.TokenDTO;
import com.yy.tools.JsonParserTool;
import com.yy.vo.in.ClientRegInVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试controller
 * @ClassName: TestController
 * @author: yangfeng
 * @date 2025/12/31 16:04
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static final ClientRegistrar SINGLETON_REGISTRAR =
            ClientRegistrarSingleton.getInstance();
    @Resource
    private WebClient oauthWebClient;
    @PostMapping("/registerClient")
    public String registerClient(@RequestBody ClientRegInVO vo) throws Exception {
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
        SINGLETON_REGISTRAR.registerClientServe(tokenDTO.getAccess_token(),vo);
        return "success";
    }
}
