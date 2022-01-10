package com.autumn.gateway.core.pojo.sync;

import com.autumn.gateway.core.enums.Pf4jPluginTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 插件信息
 * @since  2021-08-23 19:05
 */
@Data
public class Pf4jPluginInfo implements Serializable {

  private String pluginId;

  private String pluginPath;

  private Pf4jPluginTypeEnum type;
}
