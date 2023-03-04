package com.autumn.gateway.data.redis.listener;

import com.autumn.gateway.data.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-19 13:40
 */
@Slf4j
@Component
public class RedisKeyAllListener extends KeyAllEventMessageListener {

  @Value("${spring.redis.database:0}")
  private Integer database;

  @Autowired
  private RedisService<Object> redisService;
  /**
   * Creates new {@link MessageListener} for {@code __keyevent@*__:set} messages.
   *
   * @param listenerContainer must not be {@literal null}.
   */
  public RedisKeyAllListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
    String key = message.toString();
    // 获取事件
    String channel = new String(message.getChannel());

    final String hSetChannel = "__keyevent@" + database + "__:hset";

    if (hSetChannel.equals(channel)){

    }





    System.out.println(channel + "+++" + key + "=" + redisService.get(key));
  }
}
