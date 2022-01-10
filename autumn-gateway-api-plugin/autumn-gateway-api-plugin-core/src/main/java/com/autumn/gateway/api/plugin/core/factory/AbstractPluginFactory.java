package com.autumn.gateway.api.plugin.core.factory;

import org.pf4j.PluginWrapper;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-23 10:30
 */
public abstract class AbstractPluginFactory implements IPluginFactory {

  private PluginWrapper wrapper;

  protected AbstractPluginFactory(PluginWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * <获取插件包装>
   *
   * @param
   * @return org.pf4j.PluginWrapper
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 16:01
   */
  @Override
  public PluginWrapper getPluginWrapper() {
    return wrapper;
  }

  /**
   * <获取插件ID>
   *
   * @param
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/5 15:53
   */
  @Override
  public String getPluginId() {
    return wrapper.getPluginId();
  }
}
