package com.autumn.gateway.vertx.verticle;

import com.autumn.gateway.core.service.register.IApiRegisterService;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program autumn-gateway
 * @description 部署api同步 在application.json 中使用
 * @author qiushi
 * @since 2021-06-24:09:54
 */
@Slf4j
@Component
public class ApiRegisterDiscoverVerticle extends AbstractVerticle {

  @Autowired
  private IApiRegisterService apiRegisterService;

  @Override
  public void start() throws Exception {
    long start = System.currentTimeMillis();
    super.start();
    apiRegisterService.registerAll();
    log.info(
        "ApiSyncVerticle work has been finished,it took [{}] ms",
        System.currentTimeMillis() - start);
  }
}
