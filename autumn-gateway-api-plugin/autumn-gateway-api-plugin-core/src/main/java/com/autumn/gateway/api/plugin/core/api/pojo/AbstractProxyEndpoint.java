package com.autumn.gateway.api.plugin.core.api.pojo;

import com.autumn.gateway.api.plugin.core.enums.EndpointStatusEnum;
import com.autumn.gateway.api.plugin.core.enums.EndpointTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @program autumn-gateway
 * @description 服务节点
 * @author qiushi
 * @since 2021-06-17:10:07
 */
@Data
public abstract class AbstractProxyEndpoint implements Serializable {

  // private final Set<EndpointStatusListener> listeners = new HashSet<>();

  /** 默认权重 */
  public static int DEFAULT_WEIGHT = 1;
  /** 名称 */
  private String name;
  /** 目标地址 */
  private String target;
  /** 权重 */
  private int weight = DEFAULT_WEIGHT;
  /** 是否为后备 */
  private boolean backup;
  /** 状态 */
  private EndpointStatusEnum status = EndpointStatusEnum.UP;

  /** 类型 */
  private EndpointTypeEnum type;

  protected AbstractProxyEndpoint() {}

  protected AbstractProxyEndpoint(EndpointTypeEnum type, String name, String target) {
    this.type = type;
    this.name = name;
    this.target = target;
  }
}
