package com.yy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务启动类
 * @ClassName: AuthServerApplication
 * @author: yangfeng
 * @date: 2025/12/19 16:43
 * @version: 1.0.0
 */
@SpringBootApplication
@Slf4j
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
        log.info("\r\n" +
                "**********************************************************************\r\n" +
                "*********                                                    *********\r\n" +
                "*********                     start over                     *********\r\n" +
                "*********                                                    *********\r\n" +
                "**********************************************************************"
        );
    }
}
