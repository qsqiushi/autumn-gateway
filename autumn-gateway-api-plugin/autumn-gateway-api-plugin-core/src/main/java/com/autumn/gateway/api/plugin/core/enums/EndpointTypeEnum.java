package com.autumn.gateway.api.plugin.core.enums;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-22 09:18
 */
public enum EndpointTypeEnum {
  HTTP("http"),
  GRPC("grpc");

  private final String name;

  EndpointTypeEnum(String name) {
    this.name = name;
  }

  public String typeName() {
    return this.name;
  }
}
