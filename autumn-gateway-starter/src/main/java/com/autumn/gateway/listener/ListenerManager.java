package com.autumn.gateway.listener;

import com.autumn.gateway.core.event.EventManager;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.core.event.enums.SyncReactorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27 15:39
 */
@Component
public class ListenerManager {

  static {
    System.out.println("listener manager");
  }

  @Autowired private ApiReactorEventListener apiRegisterEventListener;

  @Autowired private SyncReactorEventListener syncReactorEventListener;

  @Autowired
  private EventManager eventManager;

  @PostConstruct
  public void subscribeForEvents() {

    eventManager.subscribeForEvents(apiRegisterEventListener, ApiReactorEvent.class);

    eventManager.subscribeForEvents(syncReactorEventListener, SyncReactorEvent.class);
  }
}
