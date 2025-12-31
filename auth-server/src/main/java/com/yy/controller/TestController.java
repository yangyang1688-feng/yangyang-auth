package com.yy.controller;

import com.yy.config.ClientRegistrar;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

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
    @Resource
    private WebClient oauthWebClient;
    @PostMapping("/registerClient")
    public String registerClient(){
        ClientRegistrar registrar = new ClientRegistrar(oauthWebClient);
        registrar.exampleRegistration("eyJraWQiOiJiZWI1ZmYyOC0zMjFiLTRlM2QtYjI3OC02NWYyZWVmOTQ5YmMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWdpc3RyYXItY2xpZW50IiwiYXVkIjoicmVnaXN0cmFyLWNsaWVudCIsIm5iZiI6MTc2NzE2Nzk4NSwic2NvcGUiOlsiY2xpZW50LmNyZWF0ZSJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwMDAiLCJleHAiOjE3NjcxNzUxODUsImlhdCI6MTc2NzE2Nzk4NSwianRpIjoiOGQwOWU5YWItYmQ2ZS00Yjk4LThlN2YtOWI5YzY4M2I4NDliIn0.UGZafVsguOrmI7vMPhL45HDCAcLXGADRkvEUDBkoSiKZURAOzBzA85NKmyfFNRzPlEtoD1OMyN7UW3wS9d4Rue7IZDWNT4Sdz1XUMasZrVgQ_GIgfd5DlsgxPwZg8ezILXiTu1hrkTwzqz9tZ0z9wjs64uuz-UNiwe6ZY7fO2GVQNtfE0eEcuy9dfQYqpPCDHW-eCTu2HKInIQTd8dPhQTdkwjn9LviOobCuW75O-JzYoBmjiVxzk7s9Smzo7QlvwdMOdaKWTE-WCxXRSk9ptkplQ6jGzgkW-uk63RyiPA_u-JnxvZG9ePutmA7AUqDXsR9gFYAuACwCRgk_51OOiA");
        return "success";
    }
}
