package com.yy.service.impl;

import com.yy.service.SysUserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SysUserServiceImplTest {
    @Resource
    private SysUserService sysUserService;

    @Test
    void registerUser() {
        sysUserService.registerUser("user", "password");
    }
}