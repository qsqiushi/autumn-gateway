package com.autumn.gateway.api.plugin.divide.cache;

import java.util.Map;

import com.autumn.gateway.api.plugin.divide.pojo.UserDefinedServer;
import com.google.common.collect.Maps;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since 2021-08-26:14:12
 */
public class ServerCache {

  private ServerCache() {}

  /** 缓存API load balancer */
  public static final Map<String, DynamicServerListLoadBalancer<UserDefinedServer>> API_LOAD_BALANCER = Maps.newConcurrentMap();
}
