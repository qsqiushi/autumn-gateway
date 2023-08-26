package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 16:25
 */
@Configuration
public class ServerBeanConfig {

  private IVertxManagerService vertxManagerService;

  @Autowired
  public ServerBeanConfig(IVertxManagerService vertxManagerService) {
    this.vertxManagerService = vertxManagerService;
  }

  @Bean
  public Vertx getVertx() {
    return vertxManagerService.getVertx();
  }
}
