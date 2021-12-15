package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since create 2021-08-23:08:50
 */
@Component
public class ServerConfig {

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
