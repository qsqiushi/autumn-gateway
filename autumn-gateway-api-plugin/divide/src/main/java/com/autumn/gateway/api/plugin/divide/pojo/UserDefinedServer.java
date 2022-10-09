package com.autumn.gateway.api.plugin.divide.pojo;

import com.netflix.loadbalancer.Server;

/**
 * @author qiushi
 * @program agw
 * @description 自定义server
 * @since 2021-08-26:14:45
 */
public class UserDefinedServer extends Server {

  private String url;

  private UserDefinedProxyEndpoint userDefinedProxyEndpoint;

  public UserDefinedServer(String host, int port) {
    super(host, port);
  }

  public UserDefinedServer(String scheme, String host, int port) {

    // scheme 就是 http或者 https
    super(scheme, host, port);
  }

  public UserDefinedServer(String id) {
    super(id);
    this.url = id;
  }

  public UserDefinedServer(UserDefinedProxyEndpoint userDefinedProxyEndpoint) {
    super(userDefinedProxyEndpoint.getTarget());
    this.userDefinedProxyEndpoint = userDefinedProxyEndpoint;
  }

  public String getUrl() {
    return this.url;
  }

  public UserDefinedProxyEndpoint getUserDefinedProxyEndpoint() {
    return this.userDefinedProxyEndpoint;
  }
}
