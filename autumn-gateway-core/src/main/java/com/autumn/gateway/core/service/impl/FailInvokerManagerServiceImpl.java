package com.autumn.gateway.core.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.invoker.ApiFailInvoker;
import com.autumn.gateway.api.plugin.core.pojo.AutumnCircuitBreaker;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import org.apache.commons.lang.StringUtils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-09-13:14:09
 */
public class FailInvokerManagerServiceImpl implements IFailInvokerManagerService {

  private final Map<Api, ApiFailInvoker> apiFailInvokers = new Hashtable<>();

  @Override
  public void put(Api api, AutumnCircuitBreaker autumnCircuitBreaker) {
    if (apiFailInvokers.get(api) == null) {
      apiFailInvokers.put(api, new ApiFailInvoker());
    }
    apiFailInvokers.get(api).add(autumnCircuitBreaker);
  }

  @Override
  public void remove(Api api, AutumnCircuitBreaker autumnCircuitBreaker) {
    if (apiFailInvokers.get(api) == null) {
      apiFailInvokers.put(api, new ApiFailInvoker());
    }
    apiFailInvokers.get(api).remove(autumnCircuitBreaker);
    // 关闭熔断器
    autumnCircuitBreaker.close();
  }

  @Override
  public AutumnCircuitBreaker getCircuitBreaker(Api api) {
    if (apiFailInvokers.get(api) == null) {

      apiFailInvokers.put(api, new ApiFailInvoker());
    }
    return apiFailInvokers.get(api).getEffective();
  }

  /**
   * <根据order name 获得熔断器>
   *
   * @param api api
   * @param order 顺序
   * @param name 名称
   * @return com.qm.agw.core.plugin.pojo.AutumnCircuitBreaker
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:21
   */
  @Override
  public AutumnCircuitBreaker getCircuitBreaker(Api api, Integer order, String name) {
    if (apiFailInvokers.get(api) == null) {
      apiFailInvokers.put(api, new ApiFailInvoker());
    }
    ApiFailInvoker apiFailInvoker = apiFailInvokers.get(api);

    for (AutumnCircuitBreaker AutumnCircuitBreaker : apiFailInvoker.getCircuitBreakers()) {

      if (AutumnCircuitBreaker.getOrder().intValue() == order.intValue()
          && StringUtils.equals(name, AutumnCircuitBreaker.getCircuitBreaker().name())) {

        return AutumnCircuitBreaker;
      }
    }
    return null;
  }

  /**
   * <根据创建类移除熔断器 更新插件使用>
   *
   * @param clz 创建类
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 18:32
   */
  @Override
  public void removeByClz(Class<?> clz) {
    apiFailInvokers
        .values()
        .forEach(
            apiFailInvoker -> {
              Iterator<AutumnCircuitBreaker> iterator =
                  apiFailInvoker.getCircuitBreakers().iterator();
              while (iterator.hasNext()) {
                AutumnCircuitBreaker temp = iterator.next();
                if (temp.getClz() == clz) {
                  temp.close();
                  iterator.remove();
                }
              }
            });
  }
}
