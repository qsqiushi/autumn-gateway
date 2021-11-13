package com.autumn.gateway.api.plugin.core.enums;

/** @author qiushi */
public enum ApiPluginTypeEnum {
  SYS("系统插件"),

  PRODUCT("产品插件"),

  APPLICATION("应用插件"),

  REQUEST("请求处理插件"),

  PROXY("代理插件"),

  RESPONSE("响应处理插件");

  private final String desc;

  ApiPluginTypeEnum(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return this.desc;
  }
}
