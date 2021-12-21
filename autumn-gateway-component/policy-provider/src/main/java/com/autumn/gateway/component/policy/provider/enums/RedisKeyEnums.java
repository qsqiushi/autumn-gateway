package com.autumn.gateway.component.policy.provider.enums;

import com.autumn.gateway.data.redis.constant.CacheConstants;
import lombok.Getter;

/** @author qiushi */
@Getter
public enum RedisKeyEnums {

  // 系统策略
  AGW_SYS_POLICY(
      "agw:system:policy:settings",
      "agw:system:policy:settings",
      CacheConstants.CACHE_FOREVER,
      "AGW-SYS-POLICY"),

  // APP策略
  AGW_APP_POLICY(
      "agw:app:policy:", "agw:app:policy:%s", CacheConstants.CACHE_FOREVER, "AGW-APP-POLICY"),

  // 产品策略  产品ID
  AGW_PRODUCT_POLICY(
      "agw:product:policy:",
      "agw:product:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AGW-PRODUCT-POLICY"),

  // 产品分类策略    产品分类ID
  AGW_PRODUCT_CLASSIFY_POLICY(
      "agw:product_classify:policy:",
      "agw:product_classify:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AGW-PRODUCT_CLASSIFY-POLICY"),

  // 插件策略    插件策略ID
  AGW_PLUGIN_POLICY(
      "agw:plugin:policy:",
      "agw:plugin:policy:%s",
      CacheConstants.CACHE_FOREVER,
      "AGW-PLUGIN-POLICY"),

  // 写策略
  AGW_WRITER_POLICY(
      "agw:writer:policy", "agw:writer:policy", CacheConstants.CACHE_FOREVER, "AGW-WRITER-POLICY"),
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
