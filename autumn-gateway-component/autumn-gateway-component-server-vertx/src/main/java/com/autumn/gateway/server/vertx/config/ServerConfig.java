package com.autumn.gateway.server.vertx.config;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since create 2021-08-23:08:50
 */
@Component
public class ServerConfig {

  @Bean
  public HttpServer getHttpServer() {

    Vertx vertx = Vertx.currentContext().owner();
    HttpServer httpServer = vertx.createHttpServer();
    return httpServer;
  }

}
