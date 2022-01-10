package com.autumn.gateway.listener;

import com.autumn.gateway.core.event.EventManager;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.core.event.enums.SyncReactorEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27 15:39
 */
@Component
public class ListenerManager {

  @Resource private ApiReactorEventListener apiRegisterEventListener;

  @Resource private SyncReactorEventListener syncReactorEventListener;

  @Resource private EventManager eventManager;

  @PostConstruct
  public void subscribeForEvents() {

    eventManager.subscribeForEvents(apiRegisterEventListener, ApiReactorEvent.class);

    eventManager.subscribeForEvents(syncReactorEventListener, SyncReactorEvent.class);
  }
}
