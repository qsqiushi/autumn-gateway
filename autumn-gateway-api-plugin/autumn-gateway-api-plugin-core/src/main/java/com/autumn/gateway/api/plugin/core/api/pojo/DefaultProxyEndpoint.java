package com.autumn.gateway.api.plugin.core.api.pojo;

import com.autumn.gateway.api.plugin.core.enums.EndpointTypeEnum;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 默认后台服务节点 预留给API注册与发现插件
 * @since 2021-08-26 13:30
 */
public class DefaultProxyEndpoint extends AbstractProxyEndpoint {

  public DefaultProxyEndpoint(EndpointTypeEnum type, String name, String target) {
    super(type, name, target);
  }
}
