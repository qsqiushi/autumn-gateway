package com.autumn.gateway.api.plugin.core.api.handler;

import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;

/** @author qiushi */
public interface ReactorHandler<T, E extends Reactable> {

  /**
   * 获取处理的内容
   *
   * @return Reactable
   */
  E getReactable();

  /**
   * 处理
   *
   * @param event 事件
   */
  void handle(T event);
}
