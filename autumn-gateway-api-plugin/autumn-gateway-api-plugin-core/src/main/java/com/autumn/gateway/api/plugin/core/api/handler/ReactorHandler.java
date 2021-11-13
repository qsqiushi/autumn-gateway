package com.autumn.gateway.api.plugin.core.api.handler;

import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;

/** @author qiushi */
public interface ReactorHandler<T> {

  Reactable getReactable();

  void handle(T event);
}
