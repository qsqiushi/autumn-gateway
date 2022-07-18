package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.core.handler.IGlobalApiHandler;
import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn
 * @description
 * @since 2021-08-23:08:50
 */
@Component
public class ServerConfig {
  @Resource public IGlobalApiHandler globalApiHandler;

  @Value("${autumn.server.port:80}")
  private Integer port;

  @Value("${autumn.server.bodyLimit:150}")
  private Integer bodyLimit;

  private static ServerConfig serverConfig;

  @PostConstruct
  public void init() {
    serverConfig = this;
    serverConfig.globalApiHandler = this.globalApiHandler;
    serverConfig.port = this.port;
    serverConfig.bodyLimit = this.bodyLimit;
  }

  public static IGlobalApiHandler getGlobalApiHandler() {
    return serverConfig.globalApiHandler;
  }

  public static Integer getPort() {
    return serverConfig.port;
  }

  public static Integer getBodyLimit() {
    return serverConfig.bodyLimit;
  }

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
