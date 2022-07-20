package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 16:25
 */
@Component
public class ServerBeanConfig {

  @Resource private IVertxManagerService vertxManagerService;

  @Bean
  public Vertx getVertx() {
    return vertxManagerService.getVertx();
  }

  @Bean
  public HttpServer getHttpServer() {

    Vertx vertx = vertxManagerService.getVertx();
    HttpServer httpServer = vertx.createHttpServer();
    return httpServer;
  }
}
