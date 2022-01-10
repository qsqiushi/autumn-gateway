package com.autumn.gateway.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.service.IApiHandlerProcessService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since  2021-09-08 15:42
 */
@Service
public class ApiHandlerProcessServiceImpl implements IApiHandlerProcessService {

  private Map<Api, AtomicInteger> apiProcessMap = new HashMap<>();
  /**
   * <计算正在处理API的数量>
   *
   * @param api api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  @Override
  public int getProcessNum(Api api) {
    AtomicInteger atomicInteger = apiProcessMap.get(api);
    if (atomicInteger == null) {
      return 0;
    } else {
      return atomicInteger.get();
    }
  }

  /**
   * <正在处理API的数量+1>
   *
   * @param api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  @Override
  public void processIncr(Api api) {

    AtomicInteger atomicInteger = apiProcessMap.get(api);
    if (atomicInteger == null) {
      atomicInteger = new AtomicInteger();
      apiProcessMap.put(api, atomicInteger);
    }

    atomicInteger.incrementAndGet();
  }

  /**
   * <计算正在处理API的数量-1>
   *
   * @param api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  @Override
  public void processDecr(Api api) {
    AtomicInteger atomicInteger = apiProcessMap.get(api);
    if (atomicInteger == null) {
      atomicInteger = new AtomicInteger();
      apiProcessMap.put(api, atomicInteger);
    }
    atomicInteger.decrementAndGet();
  }
}
