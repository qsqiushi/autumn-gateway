package com.autumn.gateway.core.server;

import com.autumn.gateway.core.service.server.IServer;
import io.vertx.core.Future;

import java.util.List;

/**
 * 伺服服务管理器
 *
 * @author qius 2021/7/9
 */
public interface IServerManager {
  /**
   * <新增服务>
   *
   * @param server
   * @return : java.util.List<IServer>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 10:30
   */
  List<IServer> addServer(IServer server);
  /**
   * <启动所有server>
   *
   * @param
   * @return : java.util.List<IServer>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:42
   */
  Future<List<IServer>> startServers();
  /**
   * <停止单个server>
   *
   * @param server
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:42
   */
  void stopServer(IServer server);
  /**
   * <停止所有服务>
   *
   * @param
   * @return : io.vertx.core.Future<java.lang.Void>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:42
   */
  Future<Void> stopServers();
  /**
   * <查询server的数量>
   *
   * @param
   * @return : java.lang.Integer
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:42
   */
  Integer getSize();
  /**
   * <移除server>
   *
   * @param server
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/9 15:05
   */
  void remove(IServer server);
  /**
   * <移除所有server>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:41
   */
  void removeAll();
}
