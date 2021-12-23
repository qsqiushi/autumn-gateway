package com.autumn.gateway.vertx.verticle;

import com.autumn.gateway.service.IPf4jPluginManagerService;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program autumn-gateway
 * @description 插件加载模块 在application.json 中使用
 * @author qiushi
 * @since 2021-07-06:18:16
 */
@Slf4j
@Component
public class PluginLoadVerticle extends AbstractVerticle {

  @Resource private IPf4jPluginManagerService pluginManagerService;

  @Override
  public void start() throws Exception {

    long start = System.currentTimeMillis();
    super.start();
    // TODO 从系统策略 获得插件配置信息 加载了哪些插件
    pluginManagerService.loadApiPlugin();
    log.info(
        "PluginLoadVerticle work has been finished, it took [{}]ms",
        System.currentTimeMillis() - start);
  }
}
