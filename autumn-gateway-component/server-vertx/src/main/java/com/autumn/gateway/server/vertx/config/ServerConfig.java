package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.core.handler.IGlobalApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author qiushi
 * @program autumn
 * @description
 * @since 2021-08-23:08:50
 */
@Component
public class ServerConfig {
  @Autowired
  public IGlobalApiHandler globalApiHandler;

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


}
