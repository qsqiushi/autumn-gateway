package com.autumn.gateway.server.vertx.service.impl;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.server.vertx.service.ZookeeperProperties;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program autumn
 * @description vertx管理策略实现
 * @author qiushi
 * @since 2021-07-22:15:18
 */
@Slf4j
@Service
public class VertxManagerServiceImpl implements IVertxManagerService {

  private static Vertx vertx;

  @Resource private ZookeeperProperties zookeeperProperties;

  /**
   * <获取唯一的vertx实例>
   *
   * @return : io.vertx.core.Vertx
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/22 13:34
   */
  @Override
  public synchronized Vertx getVertx() {
    if (vertx == null) {
      synchronized (VertxManagerServiceImpl.class) {
        if (vertx == null) {
          vertx = Vertx.vertx();
        }
      }
    }
    return vertx;
  }
}
