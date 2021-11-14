package com.autumn.gateway.starter.api.config;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.invoker.EndpointInvoker;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-25:15:41
 */
@Component
public class ApiContextConfig {

  @Resource private Api api;

  @Resource private IFailInvokerManagerService failInvokerManagerService;

  /**
   * 预留 目前按照插件策略获取API 服务
   *
   * @return
   */
  @Bean
  public EndpointInvoker endpointInvoker() {
    return new EndpointInvoker(api.getEndPoints());
  }
}
