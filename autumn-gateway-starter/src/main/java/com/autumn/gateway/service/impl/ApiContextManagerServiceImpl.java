package com.autumn.gateway.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.service.IApiContextManagerService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-09-08 14:27
 */
@Service
public class ApiContextManagerServiceImpl implements IApiContextManagerService {

  private Map<Api, AbstractApplicationContext> apiContextMap = new HashMap<>();

  @Override
  public AbstractApplicationContext getApplicationContext(Api api) {
    return apiContextMap.get(api);
  }

  @Override
  public Collection<AbstractApplicationContext> getApplicationContexts() {
    return apiContextMap.values();
  }

  /**
   * <注册>
   *
   * @param api api
   * @param applicationContext 上下文
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:31
   */
  @Override
  public void register(Api api, AbstractApplicationContext applicationContext) {
    apiContextMap.put(api, applicationContext);
  }

  /**
   * <解绑>
   *
   * @param api api
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 14:32
   */
  @Override
  public void unRegister(Api api) {
    apiContextMap.remove(api);
  }
}
