package com.autumn.gateway.core.service.policy.provider;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 基于redis的策略提供器
 * @since create 2021-08-12:18:26
 */
public interface IRedisPolicyProvider extends IPolicyProvider {

  /**
   * 初始化策略供应者
   *
   * @param redisTemplate
   */
  void init(RedisTemplate redisTemplate);
}
