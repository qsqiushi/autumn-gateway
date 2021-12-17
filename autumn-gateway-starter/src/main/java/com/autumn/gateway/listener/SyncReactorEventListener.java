package com.autumn.gateway.listener;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.event.Event;
import com.autumn.gateway.core.event.EventListener;
import com.autumn.gateway.core.event.EventManager;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.core.event.enums.SyncReactorEvent;
import com.autumn.gateway.core.event.impl.SimpleEvent;
import com.autumn.gateway.core.pojo.sync.Pf4jPluginInfo;
import com.autumn.gateway.core.pojo.sync.SyncMsg;
import com.autumn.gateway.core.server.IServerManager;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.core.service.plugin.AbstractAppPluginManagerService;
import com.autumn.gateway.core.service.plugin.AbstractProductClassifyPluginManagerService;
import com.autumn.gateway.core.service.plugin.AbstractProductPluginManagerService;
import com.autumn.gateway.core.service.plugin.AbstractSysPluginManagerService;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import com.autumn.gateway.service.IApiContextManagerService;
import com.autumn.gateway.service.IPf4jPluginManagerService;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-17:09:36
 */
@Service
@Slf4j
public class SyncReactorEventListener implements EventListener<SyncReactorEvent, SyncMsg> {

  @Resource private IApiDiscovererService apiDiscovererService;

  @Resource private EventManager eventManager;

  @Resource private AbstractAppPluginManagerService appPluginManagerService;

  @Resource private AbstractSysPluginManagerService sysPluginManagerService;

  @Resource private AbstractProductPluginManagerService productPluginManagerService;

  @Resource private AbstractProductClassifyPluginManagerService productClassifyPluginManagerService;

  @Resource private IServerManager serverManager;

  @Resource private IPf4jPluginManagerService pf4jPluginManagerService;

  @Resource private IApiRegisterService apiRegisterService;

  @Resource private IApiContextManagerService apiContextManagerService;

  @Override
  public void onEvent(Event<SyncReactorEvent, SyncMsg> event) {

    switch (event.type()) {
      case UNDEPLOY_API:
        undeployApi(event);
        break;
      case REFRESH_API:
        refreshApi(event);
        break;
      case REFRESH_ALL_API:
        refreshAllApi(event);
        break;
      case REFRESH_APP_POLICY:
        appPluginManagerService
            .refresh(event.content().getBizId())
            .onSuccess(event.getSuccessHandler())
            .onFailure(event.getFailureHandler());
        break;
      case REFRESH_SYS_POLICY:
        sysPluginManagerService
            .refresh(null)
            .onSuccess(event.getSuccessHandler())
            .onFailure(event.getFailureHandler());
        break;
      case REFRESH_PRODUCT_POLICY:
        productPluginManagerService
            .refresh(event.content().getBizId())
            .onSuccess(event.getSuccessHandler())
            .onFailure(event.getFailureHandler());
        ;
        break;
      case REFRESH_PRODUCT_CLASSIFY_POLICY:
        productClassifyPluginManagerService
            .refresh(event.content().getBizId())
            .onSuccess(event.getSuccessHandler())
            .onFailure(event.getFailureHandler());
        ;
        break;

      case STOP_SERVERS:
        serverManager
            .stopServers()
            .onSuccess(event.getSuccessHandler())
            .onFailure(event.getFailureHandler());
        break;
      case START_SERVERS:
        serverManager.startServers();
        break;

      case RELOAD_COMPONENT:
        reloadComponent(event);

        break;
      case RELOAD_API_PLUGIN:
        reloadApiPlugin(event);

        break;
      default:
        log.error("Unknown SyncReactorEvent");
    }
  }
  /**
   * <重新加载组件>
   *
   * @param event
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/9 13:49
   */
  private void reloadComponent(Event<SyncReactorEvent, SyncMsg> event) {

    Pf4jPluginInfo pluginInfo = event.content().getPf4jPluginInfo();

    serverManager
        .stopServers()
        // 若停止服务成功 重新加载组件
        .onSuccess(
            stopEvent -> {
              pf4jPluginManagerService
                  .reloadComponent(pluginInfo)
                  .onSuccess(
                      reloadComponentEvent -> {
                        // 若加载组件成功
                        serverManager
                            .startServers()
                            .onSuccess(event.getSuccessHandler())
                            .onFailure(event.getFailureHandler());
                      });
            });
  }

  /**
   * <重新加载API插件>
   *
   * @param event
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/9 13:49
   */
  private void reloadApiPlugin(Event<SyncReactorEvent, SyncMsg> event) {

    Future.future(
            reloadApiPluginEvent -> {
              Pf4jPluginInfo pluginInfo = event.content().getPf4jPluginInfo();

              Collection<AbstractApplicationContext> apiContexts =
                  apiContextManagerService.getApplicationContexts();

              apiContexts.forEach(AbstractApplicationContext::close);

              pf4jPluginManagerService.reloadApiPlugin(pluginInfo);

              Collection<Api> apis = apiDiscovererService.apis();

              apis.forEach(api -> apiRegisterService.reRegister(api));

              event.content();
            })
        .onSuccess(event.getSuccessHandler())
        .onFailure(event.getFailureHandler());
  }
  /**
   * <刷新所有API>
   *
   * @author qiushi
   * @since 2021/8/25 08:58
   */
  private void refreshAllApi(Event<SyncReactorEvent, SyncMsg> event) {

    Collection<Api> apis = apiDiscovererService.apis();

    apis.forEach(
        api ->
            eventManager.publishEvent(
                new SimpleEvent<>(
                    ApiReactorEvent.UPDATE,
                    api,
                    event.getSuccessHandler(),
                    event.getFailureHandler())));
  }
  /**
   * <取消指定API>
   *
   * @param event 事件
   * @author qiushi
   * @since 2021/8/25 09:01
   */
  private void undeployApi(Event<SyncReactorEvent, SyncMsg> event) {

    Api api = apiDiscovererService.get(event.content().getApiUrl());
    if (api == null) {
      return;
    }
    eventManager.publishEvent(
        new SimpleEvent<>(
            ApiReactorEvent.UNDEPLOY, api, event.getSuccessHandler(), event.getFailureHandler()));
  }
  /**
   * <刷新指定API>
   *
   * @param event 事件
   * @author qiushi
   * @since 2021/8/25 09:01
   */
  private void refreshApi(Event<SyncReactorEvent, SyncMsg> event) {

    Api api = apiDiscovererService.get(event.content().getApiUrl());
    if (api == null) {
      return;
    }
    eventManager.publishEvent(
        new SimpleEvent<>(
            ApiReactorEvent.UPDATE, api, event.getSuccessHandler(), event.getFailureHandler()));
  }
}
