package com.autumn.gateway.api.plugin.core.enums;

/**
 * @author qiushi
 */
public enum ApiPluginTypeEnum {
  /** 请求处理插件 */
  REQUEST("请求处理插件"),

  /** 代理插件 */
  PROXY("代理插件"),

  /** 响应处理插件 */
  RESPONSE("响应处理插件"),

  /** 日志插件 */
  LOG("日志插件");

  private final String desc;

  ApiPluginTypeEnum(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return this.desc;
  }
}
