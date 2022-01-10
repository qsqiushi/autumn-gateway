package com.autumn.gateway.core.service.cluster;

import com.autumn.gateway.core.service.IService;
import io.vertx.core.Vertx;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-22 13:33
 */
public interface IVertxManagerService extends IService {

  /**
   * <>
   *
   * @param
   * @return : io.vertx.core.Vertx
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/22 13:34
   */
  Vertx getVertx();
}
