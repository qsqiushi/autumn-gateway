package com.autumn.gateway.core.event.impl;

import com.autumn.gateway.core.event.Event;
import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27:14:40
 */
@Slf4j
public class SimpleEvent<T extends Enum, S> implements Event<T, S> {

  private static final Handler emptyHandler = event -> {};

  private static final Handler<Throwable> emptyFailureHandler =
      throwable -> log.error("event handler fail", throwable);

  private final T type;
  private final S content;

  private Handler<Void> successHandler;

  private Handler<Throwable> failureHandler;

  public SimpleEvent(T type, S content) {
    this.type = type;
    this.content = content;
  }

  public SimpleEvent(T type, S content, Handler successHandler, Handler<Throwable> failureHandler) {
    this.type = type;
    this.content = content;
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
  }

  @Override
  public S content() {
    return this.content;
  }

  @Override
  public T type() {
    return this.type;
  }

  @Override
  public Handler getSuccessHandler() {
    return successHandler == null ? emptyHandler : successHandler;
  }

  @Override
  public Handler<Throwable> getFailureHandler() {
    return failureHandler == null ? emptyFailureHandler : failureHandler;
  }
}
