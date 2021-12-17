package com.autumn.gateway.service.impl;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.IPluginFactoryExtensionPoint;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.core.pojo.sync.Pf4jPluginInfo;
import com.autumn.gateway.service.IPf4jPluginManagerService;
import com.autumn.gateway.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.List;

/**
 * @program autumn-gateway
 * @description 插件管理类
 * @author qiushi
 * @since create 2021-07-06:09:36
 */
@Slf4j
@Service
public class Pf4jPluginManagerServiceImpl implements IPf4jPluginManagerService {

  @Resource private SpringPluginManager springPluginManager;

  /**
   * <加载插件>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:44
   */
  @Override
  public void loadApiPlugin() {
    // TODO 修改 根据路径加载
    // 启动目录下策略插件 可以重复调用 会判断插件状态
    springPluginManager.startPlugins();
    // 获取启动的pf4j插件
    List<PluginWrapper> startedPlugins = springPluginManager.getStartedPlugins();

    if (!CollectionUtils.isEmpty(startedPlugins)) {
      for (PluginWrapper pluginWrapper : startedPlugins) {
        // 筛选API插件
        List<IPluginFactoryExtensionPoint> pluginFactoryExtensionPoints =
            springPluginManager.getExtensions(
                IPluginFactoryExtensionPoint.class, pluginWrapper.getPluginId());

        if (!CollectionUtils.isEmpty(pluginFactoryExtensionPoints)) {
          // 获取插件工厂
          IPluginFactory pluginFactory = pluginFactoryExtensionPoints.get(0).createFactory();
          // 将工厂注册成bean
          if (pluginFactory != null) {
            ApplicationContextUtil.register(pluginFactory.getPluginId(), pluginFactory);
          }
        }
      }
    }
  }

  /**
   * <是否加载了插件>
   *
   * @param pluginId 插件ID
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/7 13:26
   */
  @Override
  public Boolean hasStartPf4jPlugin(String pluginId) {

    PluginWrapper pluginWrapper = springPluginManager.getPlugin(pluginId);

    if (pluginWrapper == null) {
      return Boolean.FALSE;
    }
    return pluginWrapper.getPluginState() == PluginState.STARTED;
  }

  /**
   * <重新加载插件>
   *
   * @param info 插件绝对路径
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 13:34
   */
  @Override
  public void reloadApiPlugin(Pf4jPluginInfo info) {

    // 去掉插件的注册
    ApplicationContextUtil.destroySingleton(info.getPluginId());

    // 卸载API插件的pf4j插件
    springPluginManager.unloadPlugin(info.getPluginId());

    String pluginId = springPluginManager.loadPlugin(Paths.get(info.getPluginPath()));

    springPluginManager.startPlugin(pluginId);

    List<IPluginFactoryExtensionPoint> pluginFactoryExtensionPoints =
        springPluginManager.getExtensions(IPluginFactoryExtensionPoint.class, pluginId);

    if (!CollectionUtils.isEmpty(pluginFactoryExtensionPoints)) {

      IPluginFactory pluginFactory = pluginFactoryExtensionPoints.get(0).createFactory();

      if (pluginFactory != null) {

        ApplicationContextUtil.register(pluginFactory.getPluginId(), pluginFactory);
      }
    }
  }

  /**
   * <卸载插件>
   *
   * @param info 插件ID
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:44
   */
  @Override
  public void unloadApiPlugin(Pf4jPluginInfo info) {
    // 移除bean
    ApplicationContextUtil.destroySingleton(info.getPluginId());

    // 卸载插件
    springPluginManager.unloadPlugin(info.getPluginId());
  }

  /**
   * <创建插件>
   *
   * @param pluginId 插件ID
   * @param pluginParam 插件配置
   * @return : com.qm.qos.core.plugin.IPlugin
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 09:45
   */
  @Override
  public IPlugin createPluginInstance(String pluginId, PluginParam pluginParam) {

    IPluginFactory pluginFactory = (IPluginFactory) ApplicationContextUtil.getBean(pluginId);

    return pluginFactory.create(pluginParam);
  }
}
