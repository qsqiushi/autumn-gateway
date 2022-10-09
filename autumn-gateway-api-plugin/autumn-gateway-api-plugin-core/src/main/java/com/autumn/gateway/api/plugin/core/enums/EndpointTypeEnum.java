package com.autumn.gateway.api.plugin.core.enums;

/**
 * @program qm-gateway
 * @description
 * @author qiushi
* @since 2021-06-22:09:18
 */
public enum EndpointTypeEnum {
  /***
   * HTTP 去异味
   * @author 李鹤
   * @time 2022/7/1 9:59
   */
  HTTP("http"),
  /***
   * GRPC 去异味
   * @author 李鹤
   * @time 2022/7/1 9:59
   */
  GRPC("grpc");

  private final String name;

  EndpointTypeEnum(String name) {
    this.name = name;
  }

  public String typeName() {
    return this.name;
  }
}
