package com.autumn.gateway.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <util 上下文工具类>
 *
 * @author qiushi
 * @since 2021/3/3 17:24
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  /**
   * <获取applicationContext>
   *
   * @param
   * @return : org.springframework.context.ApplicationContext
   * @author qiushi
   * @updator qiushi
   * @since 2021/3/3 17:24
   */
  public static ApplicationContext getApplicationContext() {

    return applicationContext;
  }

  public static DefaultListableBeanFactory getBeanFactory() {
    return (DefaultListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (ApplicationContextUtil.applicationContext == null) {
      ApplicationContextUtil.applicationContext = applicationContext;
    }
  }

  /**
   * <通过name获取 Bean.>
   *
   * @param name
   * @return : java.lang.Object
   * @author qiushi
   * @updator qiushi
   * @since 2021/3/3 17:25
   */
  public static Object getBean(String name) {

    return getApplicationContext().getBean(name);
  }

  /**
   * <通过class获取Bean.>
   *
   * @param clazz
   * @return : T
   * @author qiushi
   * @updator qiushi
   * @since 2021/3/3 17:25
   */
  public static <T> T getBean(Class<T> clazz) {

    return getApplicationContext().getBean(clazz);
  }

  /**
   * <通过name,以及Clazz返回指定的Bean>
   *
   * @param name
   * @param clazz
   * @return : T
   * @author qiushi
   * @updator qiushi
   * @since 2021/3/3 17:25
   */
  public static <T> T getBean(String name, Class<T> clazz) {

    return getApplicationContext().getBean(name, clazz);
  }
  /**
   * <获取实现接口的所有bean>
   *
   * @param clazz
   * @return : java.util.Map<java.lang.String,T>
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/9 09:17
   */
  public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {

    return getApplicationContext().getBeansOfType(clazz);
  }

  public static void register(String beanName, Object bean) {
    // 注册之前先销毁
    // 多次销毁或者没有就销毁不会有问题
    destroySingleton(beanName);
    DefaultListableBeanFactory defaultListableBeanFactory = getBeanFactory();
    defaultListableBeanFactory.registerSingleton(beanName, bean);
  }

  public static void destroySingleton(String beanName) {
    DefaultListableBeanFactory defaultListableBeanFactory = getBeanFactory();
    defaultListableBeanFactory.destroySingleton(beanName);
  }
}
