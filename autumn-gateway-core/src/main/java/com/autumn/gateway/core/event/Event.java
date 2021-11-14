package com.autumn.gateway.core.event;

import io.vertx.core.Handler;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27:14:38
 */
public interface Event<T extends Enum, S> {

  /**
   * <事件内容>
   *
   * @param
   * @return : S
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/19 14:54
   */
  S content();
  /**
   * <事件类型>
   *
   * @param
   * @return : T
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/19 14:55
   */
  T type();

  Handler getSuccessHandler();

  Handler<Throwable> getFailureHandler();
}
