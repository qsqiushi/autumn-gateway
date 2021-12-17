package com.autumn.gateway.service;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.core.pojo.sync.Pf4jPluginInfo;

/** @author qiushi */
public interface IPf4jPluginManagerService {

  /**
   * <加载插件>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:44
   */
  void loadApiPlugin();

  /**
   * <是否加载了插件>
   *
   * @param pluginId 插件ID
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/7 13:26
   */
  Boolean hasStartPf4jPlugin(String pluginId);

  /**
   * <重新加载API插件>
   *
   * @param info
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 13:34
   */
  void reloadApiPlugin(Pf4jPluginInfo info);

  /**
   * <卸载插件>
   *
   * @param info
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:44
   */
  void unloadApiPlugin(Pf4jPluginInfo info);

  /**
   * <创建插件>
   *
   * @param pluginId
   * @param pluginParam
   * @return : com.qm.qos.core.plugin.IPlugin
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:45
   */
  IPlugin createPluginInstance(String pluginId, PluginParam pluginParam);
}
