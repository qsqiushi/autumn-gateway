package com.autumn.gateway.core.service.provider;

import org.pf4j.ExtensionPoint;

/** @author qiushi */
public interface IServiceProviderExtensionPoint extends ExtensionPoint {
  /**
   * <创建工厂>
   *
   * @param
   * @return : com.qm.qos.core.plugin.IPluginFactory
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/5 14:34
   */
  IServiceProvider createProvider();
}
