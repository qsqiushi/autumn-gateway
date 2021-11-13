package com.autumn.gateway.api.plugin.core;

import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import org.pf4j.ExtensionPoint;

/** @author qiushi */
public interface IPluginFactoryExtensionPoint extends ExtensionPoint {
  /**
   * <创建工厂>
   *
   * @param
   * @return
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/5 14:34
   */
  IPluginFactory createFactory();
}
