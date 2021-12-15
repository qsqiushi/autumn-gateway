package com.autumn.gateway.server.vertx.service.impl;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.server.vertx.properties.ZkClusterConfig;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program agw
 * @description vertx管理策略实现
 * @author qiushi
 * @since 2021-07-22:15:18
 */
@Slf4j
@Service
public class VertxManagerServiceImpl implements IVertxManagerService {

  private static Vertx vertx;

  @Resource private ZkClusterConfig zkClusterConfig;

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
          vertx = getZkVertx();
        }
      }
    }
    return vertx;
  }

  /**
   * <zk集群 获取vertx>
   *
   * @param
   * @return : io.vertx.core.Vertx
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/14 08:39
   */
  private Vertx getZkVertx() {

    AtomicReference<Vertx> vertxAtomicReference = new AtomicReference<>();

    ZookeeperClusterManager mgr = new ZookeeperClusterManager();

    mgr.setConfig(new JsonObject(Json.encode(zkClusterConfig)));

    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(
        options,
        res -> {
          if (res.succeeded()) {
            vertxAtomicReference.set(res.result());
            log.info("zk vertx register success");
          } else {
            log.error(
                "zk vertx register failed,return a non clustered instance using default options");
            vertxAtomicReference.set(Vertx.vertx());
          }
        });

    while (vertxAtomicReference.get() == null) {
      continue;
    }
    return vertxAtomicReference.get();
  }
}
