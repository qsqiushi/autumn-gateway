package com.autumn.gateway.api.plugin.core.service;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.pojo.AutumnCircuitBreaker;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-09-13 14:09
 */
public interface IFailInvokerManagerService {
  /**
   * <添加熔断器>
   *
   * @param api
   * @param autumnCircuitBreaker
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:23
   */
  void put(Api api, AutumnCircuitBreaker autumnCircuitBreaker);
  /**
   * <移除熔断器>
   *
   * @param api
   * @param autumnCircuitBreaker
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:23
   */
  void remove(Api api, AutumnCircuitBreaker autumnCircuitBreaker);

  /**
   * <获取第一个的熔断器>
   *
   * @param api
   * @return
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:22
   */
  AutumnCircuitBreaker getCircuitBreaker(Api api);

  /**
   * <根据order name 获得熔断器>
   *
   * @param api
   * @param order
   * @param name
   * @return AutumnCircuitBreaker
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:21
   */
  AutumnCircuitBreaker getCircuitBreaker(Api api, Integer order, String name);
  /**
   * <根据创建类移除熔断器 更新插件使用>
   *
   * @param clz
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:32
   */
  void removeByClz(Class<?> clz);
}
