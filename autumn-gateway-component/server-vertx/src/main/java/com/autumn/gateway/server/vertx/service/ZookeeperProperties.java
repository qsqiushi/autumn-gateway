package com.autumn.gateway.server.vertx.service;

import com.autumn.gateway.server.vertx.properties.ZkClusterConfig;

/**
 * <zk配置接口>
 *
 * @author qiushi
 * @since 2022/6/21 18:30
 */
public interface ZookeeperProperties {

  /**
   * <获取zk配置>
   *
   * @return com.autumn.gateway.server.vertx.properties.ZkClusterConfig
   * @author qiushi
   * @updater qiushi
   * @since 2022/6/21 18:33
   */
  ZkClusterConfig getClusterConfig();

  /**
   * <通信路径>
   *
   * @return java.lang.String
   * @author qiushi
   * @updater qiushi
   * @since 2022/6/21 18:34
   */
  String getSyncPath();
}
