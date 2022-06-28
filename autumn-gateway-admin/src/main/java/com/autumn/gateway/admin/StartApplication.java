package com.autumn.gateway.admin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
public class StartApplication {

  public static void main(String[] args) {
    SpringApplication.run(StartApplication.class, args);
  }
}
