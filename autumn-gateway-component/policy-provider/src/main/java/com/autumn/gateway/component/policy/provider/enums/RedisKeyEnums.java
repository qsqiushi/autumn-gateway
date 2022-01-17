package com.autumn.gateway.component.policy.provider.enums;

import com.autumn.gateway.data.redis.constant.CacheConstants;
import lombok.Getter;

/** @author qiushi */
@Getter
public enum RedisKeyEnums {

  // 系统策略
  AUTUMN_SYS_POLICY(
      "autumn:system:policy:settings",
      "autumn:system:policy:settings",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-SYS-POLICY"),

  // APP策略
  AUTUMN_APP_POLICY(
      "autumn:app:policy:",
      "autumn:app:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-APP-POLICY"),

  // 产品策略  产品ID
  AUTUMN_PRODUCT_POLICY(
      "autumn:product:policy:",
      "autumn:product:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-PRODUCT-POLICY"),

  // 产品分类策略    产品分类ID
  AUTUMN_PRODUCT_CLASSIFY_POLICY(
      "autumn:product_classify:policy:",
      "autumn:product_classify:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-PRODUCT_CLASSIFY-POLICY"),

  // 插件策略    插件策略ID
  AUTUMN_PLUGIN_POLICY(
      "autumn:plugin:policy:",
      "autumn:plugin:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-PLUGIN-POLICY"),

  // 写策略
  AUTUMN_WRITER_POLICY(
      "autumn:writer:policy",
      "autumn:writer:policy",
      CacheConstants.CACHE_FOREVER,
      "AUTUMN-WRITER-POLICY"),
  ;

  private final String prefix;

  private final String keyPattern;

  private final Long expireTime;

  private final String desc;

  RedisKeyEnums(String prefix, String keyPattern, Long expireTime, String desc) {
    this.prefix = prefix;
    this.keyPattern = keyPattern;
    this.expireTime = expireTime;
    this.desc = desc;
  }
}
