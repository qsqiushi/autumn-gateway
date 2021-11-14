package com.autumn.gateway.core.configer.impl;

import com.autumn.gateway.core.configer.IConfiger;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 基础配置
 *
 * @author sundc 2021/7/9
 */
@Slf4j
public class BaseConfiger implements IConfiger {

  private static final String SLASH_POINT = "\\.";

  protected JsonObject env;

  /**
   * 把传入的内容作为系统设置
   *
   * @param client 传入的设置
   */
  public BaseConfiger init(JsonObject client) {
    env = client;
    return this;
  }

  @Override
  public String getStrProperty(String key) {
    Object prop = getPropertyVal(key);
    return Optional.ofNullable(prop).map(Object::toString).orElse(null);
  }

  @Override
  public <T> T getProperty(String key, Class<T> clazz) {
    Object prop = getPropertyVal(key);
    return (T) prop;
  }

  @Override
  public Object getPropertyVal(String key) {
    if (StringUtils.isNotEmpty(key)) {
      Object partItem = env;
      for (String partKey : key.split(SLASH_POINT)) {
        if (!(partItem instanceof JsonObject)) {
          return null;
        }

        JsonObject item = (JsonObject) partItem;
        if (!item.containsKey(partKey)) {
          return null;
        }

        partItem = item.getValue(partKey);
      }
      return partItem;
    }

    return null;
  }
}
