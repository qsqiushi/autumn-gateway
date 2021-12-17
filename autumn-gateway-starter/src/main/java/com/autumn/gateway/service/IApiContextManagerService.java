package com.autumn.gateway.service;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Collection;

/**
 * <>
 *
 * @author qiushi
 * @since 2021/9/8 14:27
 */
public interface IApiContextManagerService {

  /**
   * <根据API获得API上下文>
   *
   * @param api
   * @return org.springframework.context.support.AbstractApplicationContext
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:29
   */
  AbstractApplicationContext getApplicationContext(Api api);
  /**
   * <获取所有API的上下文>
   *
   * @param
   * @return java.util.Collection<org.springframework.context.support.AbstractApplicationContext>
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:29
   */
  Collection<AbstractApplicationContext> getApplicationContexts();
  /**
   * <注册>
   *
   * @param api api
   * @param applicationContext 上下文
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:31
   */
  void register(Api api, AbstractApplicationContext applicationContext);
  /**
   * <解绑>
   *
   * @param api api
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:32
   */
  void unRegister(Api api);
}
