package com.autumn.gateway.server.vertx.service.impl;

import com.autumn.gateway.core.service.server.IServer;
import com.autumn.gateway.server.vertx.verticle.StartVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program autumn
 * @description
 * @author qiushi
 * @since 2021-08-10:09:19
 */
@Slf4j
@Service
public class VertxServerImpl implements IServer {

  @Resource private Vertx vertx;

  @Resource private StartVerticle startVerticle;

  @Resource private HttpServer httpServer;

  private Boolean started = false;

  /**
   * <f服务是否启动>
   *
   * @return : boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/23 09:51
   */
  @Override
  public boolean isStarted() {
    return started;
  }

  /**
   * <启动服务>
   *
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 09:17
   */
  @Override
  public void start() {

    if (isStarted()) {
      log.info("server has bean started...");
      return;
    }
    // 启动
    DeploymentOptions deploymentOptions =
        new DeploymentOptions()
            .setMaxWorkerExecuteTime(3000)
            .setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

    vertx.deployVerticle(startVerticle, deploymentOptions);

    started = true;
  }

  /**
   * <停止服务>
   *
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 09:17
   */
  @Override
  public void stop() {

    if (!isStarted()) {
      log.info("server has bean stopped");
    }
    httpServer.close();

    started = false;
  }
}
