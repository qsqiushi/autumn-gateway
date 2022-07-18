package com.autumn.gateway.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @program agw
 * @description 启动类
 * @author qiushi
 * @since 2021-07-21:11:08
 */
@SpringBootApplication(
    exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@ComponentScan(
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = {}))
@MapperScan("com.autumn.gateway.admin.user.mapper")
public class StartApplication {

  public static void main(String[] args) {
    SpringApplication.run(StartApplication.class, args);
  }
}
