package com.autumn.gateway.data.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @program airlook-build
 * @description redis过期监听
 *     实现KeyExpirationEventMessageListener接口，查看源码发现，该接口监听所有db的过期事件keyevent@*:expired"
 * @author qius
 * @since 2020-11-19:16:35
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

  /**
   * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
   *
   * @param listenerContainer must not be {@literal null}.
   */
  public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
    String expiredKey = message.toString();
    log.info("失效的key[{}]", expiredKey);
  }
}
