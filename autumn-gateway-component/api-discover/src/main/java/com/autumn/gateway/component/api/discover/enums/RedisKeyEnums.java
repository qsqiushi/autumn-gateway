package com.autumn.gateway.component.api.discover.enums;

import com.autumn.gateway.data.redis.constant.CacheConstants;
import lombok.Getter;

/** @author qiushi */
@Getter
public enum RedisKeyEnums {

  /** 当前登录账号映射用户的详细信息 */
  GATEWAY_API_URL(
      "autumn:gateway:api:url&",
      "autumn:gateway:api:url&%s",
      CacheConstants.CACHE_FOREVER,
      "API-URL"),
  ;

  /** 前缀 */
  private final String prefix;
  /** 拼接 */
  private final String keyPattern;
  /** 有效期 */
  private final Long expireTime;
  /** 描述 */
  private final String desc;

  RedisKeyEnums(String prefix, String keyPattern, Long expireTime, String desc) {
    this.prefix = prefix;
    this.keyPattern = keyPattern;
    this.expireTime = expireTime;
    this.desc = desc;
  }
}
