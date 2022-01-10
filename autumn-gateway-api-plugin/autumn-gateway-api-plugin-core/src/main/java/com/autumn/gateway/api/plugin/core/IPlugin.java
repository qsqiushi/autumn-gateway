package com.autumn.gateway.api.plugin.core;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
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
   * @param
   * @return :
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/24 19:15
   */
  ApiPluginTypeEnum getPluginType();

  /**
   * <执行>
   *
   * @param simpleExecutionContext
   * @param handler
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/5 14:54
   */
  void execute(SimpleExecutionContext simpleExecutionContext, ReactorHandler handler);
  /**
   * <如果上下文中存在错误是否继续执行>
   *
   * @param
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:10
   */
  Boolean errorEncounteredContinue();

  /**
   * <>
   *
   * @param
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:48
   */
  String getPluginId();
}
