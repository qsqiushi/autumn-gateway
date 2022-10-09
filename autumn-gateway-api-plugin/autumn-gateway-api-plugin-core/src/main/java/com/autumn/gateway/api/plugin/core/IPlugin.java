package com.autumn.gateway.api.plugin.core;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-01 16:05
 */
public interface IPlugin {

  /**
   * <获得插件类型>
   *
   * @return ApiPluginTypeEnum
   * @author qiushi
   * @updater qiushi
   * @since 2021/6/24 19:15
   */
  ApiPluginTypeEnum getPluginType();

  /**
   * <执行>
   *
   * @param simpleExecutionContext 上下文
   * @param handler handler
   * @author qiushi
   * @updater qiushi
   * @since 2021/6/5 14:54
   */
  void execute(
          SimpleExecutionContext simpleExecutionContext,
          ReactorHandler<SimpleExecutionContext, Api> handler);
  /**
   * <如果上下文中存在错误是否继续执行>
   *
   * @return java.lang.Boolean
   * @author qiushi
   * @updater qiushi
   * @since 2021/9/7 09:10
   */
  Boolean errorEncounteredContinue();

  /**
   * <>
   *
   * @return java.lang.String
   * @author qiushi
   * @updater qiushi
   * @since 2021/9/7 09:48
   */
  String getPluginId();
}
