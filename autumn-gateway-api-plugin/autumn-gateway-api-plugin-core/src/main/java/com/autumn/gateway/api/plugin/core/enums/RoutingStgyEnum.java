package com.autumn.gateway.api.plugin.core.enums;

/**
 * @author qiushi
 * @program agw
 * @description 路由策略枚举
 * @since 2022-02-17 14:03
 */
public enum RoutingStgyEnum {
  /** 单实例策略 */
  SINGLE_RULE(0),
  /** 可用过滤策略 */
  AVAILABILITY_FILTERING_RULE(1),
  /** 随机 */
  RANDOM_RULE(2),
  /** 重试策略 */
  RETRY_RULE(3),
  /** 响应时间权重策略 */
  WEIGHTED_RESPONSE_TIME_RULE(4),
  /** 线性轮训 */
  ROUND_ROBIN_RULE(5),
  /** 最少连接策略 */
  BEST_AVAILABLE_RULE(6),
  /** 条件策略 */
  CONDITION_RULE(7),
  ;

  private final int code;


  RoutingStgyEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }

  public static RoutingStgyEnum getByCode(int code) {
    for (RoutingStgyEnum type : RoutingStgyEnum.values()) {
      if (type.getCode() == code) {
        return type;
      }
    }
    return null;
  }
}
