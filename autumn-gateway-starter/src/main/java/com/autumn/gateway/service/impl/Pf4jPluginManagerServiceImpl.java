package com.autumn.gateway.service.impl;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.IPluginFactoryExtensionPoint;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.config.ApplicationConfiger;
import com.autumn.gateway.core.handler.UrlHandler;
import com.autumn.gateway.core.pojo.sync.Pf4jPluginInfo;
import com.autumn.gateway.core.server.IServerManager;
import com.autumn.gateway.core.service.IListableComponentBeanManagerService;
import com.autumn.gateway.core.service.IListableRouterManagerService;
import com.autumn.gateway.core.service.IService;
import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.core.service.policy.provider.IPolicyProviderProvider;
import com.autumn.gateway.core.service.provider.IServiceProvider;
import com.autumn.gateway.core.service.provider.IServiceProviderExtensionPoint;
import com.autumn.gateway.core.service.router.IRouterService;
import com.autumn.gateway.core.service.server.IServer;
import com.autumn.gateway.service.IPf4jPluginManagerService;
import com.autumn.gateway.util.ApplicationContextUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

  @Resource private ApplicationConfiger applicationConfiger;

  @Resource private IServerManager serverManager;

  @Resource private IListableComponentBeanManagerService listableComponentBeanManagerService;

  @Resource private IListableRouterManagerService listableRouterManagerService;

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
   * <加载组件>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/22 15:45
   */
  @Override
  public void loadComponent() {
    log.info("Loading component...");
    long start = System.currentTimeMillis();
    // 启动目录下策略插件
    springPluginManager.startPlugins();
    // 找到所有启动插件
    List<PluginWrapper> startedPlugins = springPluginManager.getStartedPlugins();
    // 获得插件参数
    if (!CollectionUtils.isEmpty(startedPlugins)) {
      for (PluginWrapper pluginWrapper : startedPlugins) {
        // 筛选组件插件  实现 IServiceProviderExtensionPoint
        List<IServiceProviderExtensionPoint> pluginFactoryExtensionPoints =
            springPluginManager.getExtensions(
                IServiceProviderExtensionPoint.class, pluginWrapper.getPluginId());

        if (!CollectionUtils.isEmpty(pluginFactoryExtensionPoints)) {
          instComponent(
              pluginWrapper.getPluginId(), pluginFactoryExtensionPoints.get(0).createProvider());
        }
      }
    }

    log.info("Loading component complete, it takes [{}] ms", System.currentTimeMillis() - start);
  }

  private void instComponent(String pluginId, IServiceProvider provider) {

    log.info("instant component...");

    // 获取组件策略
    JsonObject jsonObject = applicationConfiger.getPluginProperty(pluginId);

    if (jsonObject == null) {
      log.error("policy for [{}] component can't be found", pluginId);
      return;
    }
    // 创建组件实例
    IService service = provider.create(jsonObject);
    // 注册成bean
    if (service != null) {
      // 注册pf4j插件工厂
      register(pluginId, service);

      if (service instanceof IPolicyProviderProvider) {
        // 如果是策略提供者的提供者
        IPolicyProviderProvider policyProviderProvider = (IPolicyProviderProvider) service;
        // 册app策略提供者
        register("appPolicyProvider", policyProviderProvider.createAppPolicyProvider());
        // 注册系统策略提供者
        register("sysPolicyProvider", policyProviderProvider.createSysPolicyProvider());

        // 注册产品策略提供者
        register("productPolicyProvider", policyProviderProvider.createProductPolicyProvider());
        // 注册产品分类策略提供者
        register(
            "productClassifyPolicyProvider",
            policyProviderProvider.createProductClassifyPolicyProvider());
        // 注册插件策略提供者
        register("pluginPolicyProvider", policyProviderProvider.createPluginPolicyProvider());

        // 注册写策略提供者
        register("writerPolicyProvider", policyProviderProvider.createWriterPolicyProvider());

      } else if (service instanceof IServer) {
        // 移除所有服务
        serverManager.removeAll();
        // 如果是server
        serverManager.addServer((IServer) service);
      } else if (service instanceof IRouterService) {
        // 自定义路由
        List<UrlHandler> urlHandlers = ((IRouterService) service).userDefinedRouterHandler();
        listableRouterManagerService.registerRouters(
            urlHandlers.toArray(new UrlHandler[urlHandlers.size()]));
      } else if (service instanceof IVertxManagerService) {
        Vertx vertx = ((IVertxManagerService) service).getVertx();
        ApplicationContextUtil.register("vertx", vertx);
      }
    }
    log.info("instant component complete");
  }

  private void register(String pluginId, IService service) {
    ApplicationContextUtil.register(pluginId, service);
    listableComponentBeanManagerService.registerComponentBean(service);
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
   * <重新加载系统组件>
   *
   * @param info
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 13:34
   */
  @Override
  public Future<Void> reloadComponent(Pf4jPluginInfo info) {

    return Future.future(
        event -> {
          // 去掉插件的注册
          ApplicationContextUtil.destroySingleton(info.getPluginId());
          // 卸载组件插件
          springPluginManager.unloadPlugin(info.getPluginId());

          // 根据路径重新加载插件
          String pluginId = springPluginManager.loadPlugin(Paths.get(info.getPluginPath()));

          if (!StringUtils.equals(pluginId, info.getPluginId())) {

            log.error(
                "different component loaded ,source [{}], target [{}]",
                pluginId,
                info.getPluginId());
            // 卸载组件插件
            springPluginManager.unloadPlugin(pluginId);
            event.fail("different component loaded");
            return;
          }

          // 启动组件插件
          springPluginManager.startPlugin(pluginId);
          List<IServiceProviderExtensionPoint> serviceProviderExtensionPoints =
              springPluginManager.getExtensions(IServiceProviderExtensionPoint.class, pluginId);
          if (!CollectionUtils.isEmpty(serviceProviderExtensionPoints)) {
            // 装载组件
            instComponent(pluginId, serviceProviderExtensionPoints.get(0).createProvider());
          }
          event.complete();
        });
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
   * <卸载系统组件>
   *
   * @param info
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/6 13:34
   */
  @Override
  public void unloadComponent(Pf4jPluginInfo info) {

    IService service = ApplicationContextUtil.getBean(info.getPluginId(), IService.class);
    if (service instanceof IPolicyProviderProvider) {
      ApplicationContextUtil.destroySingleton("appPolicyProvider");
      ApplicationContextUtil.destroySingleton("sysPolicyProvider");
      ApplicationContextUtil.destroySingleton("productPolicyProvider");
      ApplicationContextUtil.destroySingleton("productClassifyPolicyProvider");
      ApplicationContextUtil.destroySingleton("pluginPolicyProvider");
      ApplicationContextUtil.destroySingleton("writerPolicyProvider");
    } else if (service instanceof IServer) {
      IServer server = (IServer) service;
      serverManager.stopServer(server);
      serverManager.remove(server);
    } else if (service instanceof IRouterService) {
      // 自定义路由
      List<UrlHandler> urlHandlers = ((IRouterService) service).userDefinedRouterHandler();
      listableRouterManagerService.removeRouters(
          urlHandlers.toArray(new UrlHandler[urlHandlers.size()]));
    }
    ApplicationContextUtil.destroySingleton(info.getPluginId());
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
