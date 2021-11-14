package com.autumn.gateway.service.impl;

import com.autumn.gateway.core.service.IListableComponentBeanManagerService;
import com.autumn.gateway.core.service.IService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 组件实现托管 service
 * @since create 2021-08-30:14:30
 */
@Service
public class ListableComponentBeanManagerServiceImpl
    implements IListableComponentBeanManagerService {

  private Map<Class, IService> beanMap = new HashMap<>();

  /**
   * <获取组件的实现>
   *
   * @param clz 类
   * @return T 泛型
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:27
   */
  @Override
  public <T> T getComponentBean(Class<T> clz) {

    Iterator<Map.Entry<Class, IService>> iter = beanMap.entrySet().iterator();

    List<T> result = new ArrayList<>();

    while (iter.hasNext()) {

      Map.Entry<Class, T> entry = (Map.Entry) iter.next();

      Class key = entry.getKey();

      if (clz.isAssignableFrom(key) || clz.equals(key)) {
        result.add(entry.getValue());
      }
    }

    if (result.size() > 1) {
      throw new RuntimeException("too many " + clz.getSimpleName());
    }

    if (result.size() < 1) {
      throw new RuntimeException("there is no " + clz.getSimpleName());
    }

    return result.get(0);
  }

  /**
   * <注册组件bean>
   *
   * @param bean
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  @Override
  public void registerComponentBean(IService bean) {
    beanMap.put(bean.getClass(), bean);
  }

  /**
   * <移除组件bean>
   *
   * @param clz
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  @Override
  public void removeComponentBean(Class clz) {
    beanMap.remove(clz);
  }

  /**
   * <移除组件bean>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 14:28
   */
  @Override
  public void removeAllComponentBean() {
    beanMap.clear();
  }
}
