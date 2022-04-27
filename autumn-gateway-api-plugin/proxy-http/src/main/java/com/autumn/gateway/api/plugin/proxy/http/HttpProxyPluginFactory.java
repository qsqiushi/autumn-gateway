package com.autumn.gateway.api.plugin.proxy.http;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.factory.AbstractPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import org.pf4j.PluginWrapper;

/**
 * @program agw
 * @description http代理插件工厂
 * @author qiushi
 * @since 2021-07-07:09:11
 */
public class HttpProxyPluginFactory extends AbstractPluginFactory {

  private IFailInvokerManagerService failInvokerManagerService;

  public HttpProxyPluginFactory(
      PluginWrapper wrapper, IFailInvokerManagerService failInvokerManagerService) {
    super(wrapper);
    this.failInvokerManagerService = failInvokerManagerService;
  }

  /**
   * <获取插件类型>
   *
   * @return : java.lang.Class
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 16:46
   */
  @Override
  public Class getPluginClass() {
    return HttpProxyPlugin.class;
  }

  /**
   * <创建插件>
   *
   * @param pluginParam
   * @return : com.qm.plugin.api.IPlugin
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 16:22
   */
  @Override
  public IPlugin create(PluginParam pluginParam) {

    return new HttpProxyPlugin(pluginParam, failInvokerManagerService);
  }

  /**
   * <获取插件配置说明>
   *
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/7 08:53
   */
  @Override
  public String getPluginConfigDes() {
    return null;
  }

  /**
   * <销毁之前调用>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/14 15:58
   */
  @Override
  public void beforeDestroy() {}
}
