package com.autumn.gateway.data.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @program airlook-build
 * @description 自定义redis监听
 * @author qius
 * @since 2020-11-19 16:40
 */
@Slf4j
public class RedisExpiredListener implements MessageListener {
  /**
   * Callback for processing received objects through Redis.
   *
   * @param message message must not be {@literal null}.
   * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
   */
  @Override
  public void onMessage(Message message, byte[] pattern) {
    // valueSerializer
    byte[] body = message.getBody();
    byte[] channel = message.getChannel();
    log.info(
        "onMessage >> channel: [{}], body: [{}], bytes: [{}]",
        new String(channel),
        new String(body),
        new String(pattern));
  }
}
