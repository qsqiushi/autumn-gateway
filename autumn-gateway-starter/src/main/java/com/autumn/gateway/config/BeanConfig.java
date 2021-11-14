package com.autumn.gateway.config;

import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import com.autumn.gateway.core.service.impl.FailInvokerManagerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-09-13:14:30
 */
@Configuration
public class BeanConfig {

  @Bean
  public IFailInvokerManagerService failInvokerManagerService() {
    return new FailInvokerManagerServiceImpl();
  }
}
