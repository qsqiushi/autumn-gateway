package com.autumn.gateway.core.service.server;

import com.autumn.gateway.core.service.IService;
import io.vertx.core.json.JsonObject;

/**
 * 伺服服务接口
 *
 * @author sundc 2021/7/9
 */
public interface IServer extends IService {
  /**
   * <f服务是否启动>
   *
   * @param
   * @return : boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/23 09:51
   */
  boolean isStarted();

  /**
   * <>
   *
   * @param serverPolicy
   * @return : IServer
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/13 13:50
   */
  IServer init(JsonObject serverPolicy);

  /**
   * <启动服务>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 09:17
   */
  void start();
  /**
   * <停止服务>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 09:17
   */
  void stop();
}
