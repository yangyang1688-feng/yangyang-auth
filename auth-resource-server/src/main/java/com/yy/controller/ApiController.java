package com.yy.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {
    
    @GetMapping("/api/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> info = new HashMap<>();
        info.put("username", jwt.getSubject());
        info.put("claims", jwt.getClaims());
        info.put("scopes", jwt.getClaim("scope"));
        return info;
    }
    
    @GetMapping("/public/health")
    public String health() {
        return "Service is healthy";
    }
}