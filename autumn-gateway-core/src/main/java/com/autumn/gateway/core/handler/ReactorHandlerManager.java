package com.autumn.gateway.core.handler;

import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;

/** @author qiushi */
public interface ReactorHandlerManager {

  /**
   * <handler注册>
   *
   * @param reactorHandler
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 09:11
   */
  void register(ReactorHandler reactorHandler);
  /**
   * <移除 reactable>
   *
   * @param reactable
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 09:21
   */
  void remove(Reactable reactable);
  /**
   * <获得 reactable>
   *
   * @param reactable
   * @return : ReactorHandler
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 09:26
   */
  ReactorHandler get(Reactable reactable);
}
