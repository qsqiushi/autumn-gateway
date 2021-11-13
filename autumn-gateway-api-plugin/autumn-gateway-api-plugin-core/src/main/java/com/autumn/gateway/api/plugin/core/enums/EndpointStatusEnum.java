package com.autumn.gateway.api.plugin.core.enums;

/**
 * <节点状态>
 *
 * @author qiushi
 * @since 2021/8/19 14:51
 */
public enum EndpointStatusEnum {
  UP(3),
  DOWN(0),
  TRANSITIONALLY_DOWN(1),
  TRANSITIONALLY_UP(2);

  private final int code;

  EndpointStatusEnum(int code) {
    this.code = code;
  }

  public int code() {
    return this.code;
  }

  public boolean isDown() {
    return this == DOWN || this == TRANSITIONALLY_UP;
  }
}
