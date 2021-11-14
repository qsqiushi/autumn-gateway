package com.autumn.gateway.api.plugin.core.api.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program autumn-gateway
 * @description 插件信息
 * @author qiushi
 * @since 2021-06-17:09:46
 */
@Data
@Accessors(chain = true)
public class PluginConfigInfo {

  /** 插件配置 schema-form.json */
  private String pluginConfig;

  private String pluginId;

  private String policyId;

  private String name;
}
