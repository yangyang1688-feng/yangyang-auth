package com.yy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 授权服务启动测试类
 * @ClassName: AuthServerApplicationTest
 * @author: yangfeng
 * @date 2025/12/19 16:46
 * @version: 1.0.0
 */
@SpringBootTest
@Slf4j
public class AuthServerApplicationTests {
    @Test
    void init(){
        log.info("init test");
    }
}
