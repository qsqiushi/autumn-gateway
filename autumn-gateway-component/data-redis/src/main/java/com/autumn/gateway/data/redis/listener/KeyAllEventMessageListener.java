package com.autumn.gateway.data.redis.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.lang.Nullable;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-19 13:18
 */
public class KeyAllEventMessageListener extends KeyspaceEventMessageListener
    implements ApplicationEventPublisherAware {
  private static final Topic KEY_EVENT_SET_TOPIC = new PatternTopic("__keyevent@*__:set");

  private static final Topic TOPIC_ALL_KEY_EVENTS = new PatternTopic("__keyevent@*");
  @Nullable private ApplicationEventPublisher publisher;

  public KeyAllEventMessageListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  @Override
  protected void doRegister(RedisMessageListenerContainer listenerContainer) {
    listenerContainer.addMessageListener(this, TOPIC_ALL_KEY_EVENTS);
  }

  @Override
  protected void doHandleMessage(Message message) {
    this.publishEvent(new RedisKeyAllEvent(message.getBody()));
  }

  protected void publishEvent(RedisKeyAllEvent event) {
    if (this.publisher != null) {
      this.publisher.publishEvent(event);
    }
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }
}
