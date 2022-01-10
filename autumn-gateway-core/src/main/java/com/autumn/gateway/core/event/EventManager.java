package com.autumn.gateway.core.event;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27 14:36
 */
public interface EventManager {

  void publishEvent(Enum type, Object content);

  void publishEvent(Event event);

  <T extends Enum> void subscribeForEvents(EventListener<T, ?> eventListener, Class<T> events);

  <T extends Enum> void subscribeForEvents(EventListener<T, ?> eventListener, T... events);
}
