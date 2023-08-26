package com.autumn.gateway.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiushi
 * @program agw
 * @description 启动类
 * @since 2021-07-21:11:08
 */
@SpringBootApplication
@MapperScan("com.autumn.gateway.admin.user.mapper")
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}
