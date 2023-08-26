package com.autumn.gateway.server.vertx.service.impl;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.server.vertx.service.ZookeeperProperties;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Autowired private ZookeeperProperties zookeeperProperties;

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
          vertx = Vertx.vertx();
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

    zookeeperProperties
        .getClusterConfig()
        .setZookeeperHosts(zookeeperProperties.getClusterConfig().getZookeeperHosts());

    mgr.setConfig(new JsonObject(Json.encode(zookeeperProperties.getClusterConfig())));

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
