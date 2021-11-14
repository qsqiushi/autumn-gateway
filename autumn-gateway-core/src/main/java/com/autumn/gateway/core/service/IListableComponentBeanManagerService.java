package com.autumn.gateway.core.service;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 可列出组件bean 控制
 * @since create 2021-08-30:14:18
 */
public interface IListableComponentBeanManagerService {

  /**
   * <获取组件的实现>
   *
   * @param clz
   * @return : T
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:27
   */
  <T> T getComponentBean(Class<T> clz);

  /**
   * <注册组件bean>
   *
   * @param bean
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  void registerComponentBean(IService bean);

  /**
   * <移除组件bean>
   *
   * @param clz
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  void removeComponentBean(Class clz);

  /**
   * <移除组件bean>
   *
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  void removeAllComponentBean();
}
