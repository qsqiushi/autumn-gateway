package com.autumn.gateway.api.plugin.core.invoker;

import com.autumn.gateway.api.plugin.core.api.pojo.AbstractProxyEndpoint;
import lombok.Data;

import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-25:15:47
 */
@Data
public class EndpointInvoker {

  private List<AbstractProxyEndpoint> endPoints;

  public EndpointInvoker(List<AbstractProxyEndpoint> endPoints) {
    this.endPoints = endPoints;
  }
}
