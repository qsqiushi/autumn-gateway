package com.autumn.gateway.api.plugin.core.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-07 10:25
 */
@Data
@Accessors(chain = true)
public class PluginInfo {

  private String pluginId;

  private String pluginZipAbsolutePath;

  /** 插件状态 0 禁用 1 启用 */
  private int status;

  /** 配置 */
  private String config;
}
