package com.autumn.gateway.server.vertx.config;

import com.autumn.gateway.server.vertx.verticle.StartVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program autumn
 * @description
 * @author qiushi
 * @since 2021-07-06:16:25
 */
@Slf4j
@Configuration
public class StandaloneConfig implements InitializingBean {

  @Resource private Vertx vertx;

  @Value("${autumn.server.instances:16}")
  private Integer instances;

  @Override
  public void afterPropertiesSet() {

    DeploymentOptions deploymentOptions =
        new DeploymentOptions()
            .setInstances(instances)
            .setMaxWorkerExecuteTime(3000)
            .setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

    vertx.deployVerticle(StartVerticle.class, deploymentOptions);
  }
}
