package com.autumn.gateway.core.handler.impl;

import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-28:16:47
 */
@Component
public class DefaultReactorHandlerManager implements ReactorHandlerManager {

  private final Map<Reactable, ReactorHandler> handlers = new HashMap<>();

  @Override
  public void register(ReactorHandler reactorHandler) {
    handlers.put(reactorHandler.getReactable(), reactorHandler);
  }

  @Override
  public void remove(Reactable reactable) {
    handlers.remove(reactable);
  }

  @Override
  public ReactorHandler get(Reactable reactable) {
    return handlers.get(reactable);
  }
}
