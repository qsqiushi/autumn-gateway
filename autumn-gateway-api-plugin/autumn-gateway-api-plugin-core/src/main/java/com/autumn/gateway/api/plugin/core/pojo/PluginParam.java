package com.autumn.gateway.api.plugin.core.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program autumn-gateway
 * @description 插件参数
 * @author qiushi
 * @since 2021-06-18:16:24
 */
@Data
@Accessors(chain = true)
public class PluginParam {
  private String jsonParam;
}
