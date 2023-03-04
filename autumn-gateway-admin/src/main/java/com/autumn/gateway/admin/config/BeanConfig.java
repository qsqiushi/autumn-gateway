package com.autumn.gateway.admin.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 13:56
 */
@Configuration
public class BeanConfig {

  static {
    System.out.println(1111);
  }

  @Bean
  public MapperFactory mapperFactory() {
    return new MapperFactory();
  }
}
