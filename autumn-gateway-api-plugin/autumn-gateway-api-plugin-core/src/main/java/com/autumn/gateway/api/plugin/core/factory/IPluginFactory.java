package com.autumn.gateway.api.plugin.core.factory;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import org.pf4j.PluginWrapper;

/**
 * @program  autumn-gateway
 * @description 插件工厂
 * @author qiushi
 * @since 2021-07-01 16:14
 */
public interface IPluginFactory {

  /**
   * <获取插件包装>
   *
   * @param
   * @return org.pf4j.PluginWrapper
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 16:01
   */
  PluginWrapper getPluginWrapper();

  /**
   * <获取插件类型>
   *
   * @param
   * @return : java.lang.Class
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 16:46
   */
  Class getPluginClass();
  /**
   * <获取插件ID>
   *
   * @param
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/5 15:53
   */
  String getPluginId();

  /**
   * 创建插件 考虑到重复插件、不同顺序、不同配置 所以没有通过注册的形式实现插件实例
   *
   * @param pluginParam 插件配置
   * @return com.autumn.plugin.api.IPlugin
   * @author qiushi
   * @since 2021/6/18 16:22
   */
  IPlugin create(PluginParam pluginParam);

  /**
   * <获取插件配置说明>
   *
   * @param
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/7 08:53
   */
  String getPluginConfigDes();
  /**
   * <销毁之前调用>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 15:58
   */
  void beforeDestroy();
}
