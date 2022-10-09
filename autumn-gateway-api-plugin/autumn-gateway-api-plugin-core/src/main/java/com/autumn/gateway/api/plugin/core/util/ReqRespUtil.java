package com.autumn.gateway.api.plugin.core.util;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;

import io.vertx.core.buffer.Buffer;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since 2022-05-25 08:48
 */
public class ReqRespUtil {

  private ReqRespUtil() {}

  /**
   * <返回响应>
   *
   * @param ctx 上下文
   * @param statusCode 响应
   * @param result 返回结果
   * @author qiushi
   * @updater qiushi
   * @since 2022/5/25 08:54
   */
  public static void endResp(SimpleExecutionContext ctx, Integer statusCode, String result) {
    ctx.getRoutingContext().response().setStatusCode(statusCode).end(result);
    ctx.setEndTime(System.currentTimeMillis());
    ctx.setStatusCode(statusCode);
    ctx.setRespResult(result);
  }
  /**
   * <返回响应>
   *
   * @param ctx 上下文
   * @param statusCode 响应
   * @param result 返回结果
   * @author qiushi
   * @updater qiushi
   * @since 2022/5/25 08:54
   */
  public static void endResp(SimpleExecutionContext ctx, Integer statusCode, Buffer result) {
    ctx.getRoutingContext().response().setStatusCode(statusCode).end(result);
    ctx.setEndTime(System.currentTimeMillis());
    ctx.setStatusCode(statusCode);
    ctx.setRespResult(result.toString());
  }
}
