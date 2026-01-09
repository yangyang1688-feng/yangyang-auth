package com.yy.ts1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 授权服务启动类
 * @ClassName: ClientTs1Application
 * @author: yangfeng
 * @date: 2025/12/19 16:43
 * @version: 1.0.0
 */
@SpringBootApplication
@Slf4j
public class ClientTs1Application {
    public static void main(String[] args) {
        SpringApplication.run(ClientTs1Application.class, args);
        log.info("\r\n" +
                "**********************************************************************\r\n" +
                "*********                                                    *********\r\n" +
                "*********                     start over                     *********\r\n" +
                "*********                                                    *********\r\n" +
                "**********************************************************************"
        );
    }
}
