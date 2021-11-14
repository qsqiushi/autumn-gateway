package com.autumn.gateway.core.event;

/**
 * <事件监听>
 *
 * @author qiushi
 * @since 2021/8/19 14:51
 */
public interface EventListener<T extends Enum, S> {

  void onEvent(Event<T, S> event);
}
