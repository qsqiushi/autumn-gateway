package com.autumn.gateway.data.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.autumn.gateway.data.redis.listener.RedisExpiredListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

/**
 * redis配置
 *
 * @author guoxin
 * @since 2019-05-31 11:15:34
 */
@Slf4j
@Configuration
public class RedisConfiguration {

  private static final StringRedisSerializer STRING_REDIS_SERIALIZER = new StringRedisSerializer();

  private static final GenericFastJsonRedisSerializer GENERIC_FAST_JSON_REDIS_SERIALIZER =
      new GenericFastJsonRedisSerializer();

  @PostConstruct
  public void init() {
    log.info("Loading AirLook Component Redis Configuration");
  }

  @Bean
  @ConditionalOnMissingBean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    // 1.创建 redisTemplate 模版
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 2.关联redisConnectionFactory
    template.setConnectionFactory(redisConnectionFactory);
    // 3.创建 序列化类
    template.setKeySerializer(STRING_REDIS_SERIALIZER);
    template.setHashKeySerializer(STRING_REDIS_SERIALIZER);

    template.setValueSerializer(GENERIC_FAST_JSON_REDIS_SERIALIZER);
    template.setHashValueSerializer(GENERIC_FAST_JSON_REDIS_SERIALIZER);
    template.afterPropertiesSet();
    return template;
  }

  //    @Bean
  //    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
  //            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
  //        final RedisSerializationContext<String, Object> build = RedisSerializationContext.
  //                <String,
  // Object>newSerializationContext(GENERIC_FAST_JSON_REDIS_SERIALIZER).build();
  //        ReactiveRedisTemplate<String, Object> reactiveRedisTemplate =
  //                new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, build);
  //        return reactiveRedisTemplate;
  //    }

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    // 监听db0的过期事件 "__keyspace@*__:expired"
    container.addMessageListener(new RedisExpiredListener(), new PatternTopic("__keyspace@*__:*"));
    return container;
  }
}
