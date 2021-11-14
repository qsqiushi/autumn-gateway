package com.autumn.gateway.core.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-31:16:09
 */
public abstract class UrlHandler implements Handler<RoutingContext> {
  /**
   * <url>
   *
   * @param
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/31 16:10
   */
  public abstract String getUrl();
  /**
   * <获取方法>
   *
   * @param
   * @return : io.vertx.core.http.HttpMethod
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 14:56
   */
  public abstract HttpMethod getMethod();

  @Override
  public boolean equals(Object o) {

    // 自反性
    if (this == o) {
      return true;
    }
    // 任何对象不等于null，比较是否为同一类型
    if (!(o instanceof UrlHandler)) {
      return false;
    }
    // 强制类型转换
    UrlHandler urlHandler = (UrlHandler) o;
    // 比较属性值
    return StringUtils.equals(urlHandler.getUrl(), this.getUrl());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUrl());
  }
}
