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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since  2021-08-17 09:36
 */
@Service
@Slf4j
public class SyncReactorEventListener implements EventListener<SyncReactorEvent, SyncMsg> {

  @Autowired
  private IApiDiscovererService apiDiscovererService;

  @Autowired private EventManager eventManager;

  @Autowired private AbstractAppPluginManagerService appPluginManagerService;

  @Autowired private AbstractSysPluginManagerService sysPluginManagerService;

  @Autowired private AbstractProductPluginManagerService productPluginManagerService;

  @Autowired private AbstractProductClassifyPluginManagerService productClassifyPluginManagerService;

  @Autowired private IServerManager serverManager;

  @Autowired private IPf4jPluginManagerService pf4jPluginManagerService;

  @Autowired private IApiRegisterService apiRegisterService;

  @Autowired private IApiContextManagerService apiContextManagerService;

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

      case RELOAD_API_PLUGIN:
        reloadApiPlugin(event);

        break;
      default:
        log.error("Unknown SyncReactorEvent");
    }
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
