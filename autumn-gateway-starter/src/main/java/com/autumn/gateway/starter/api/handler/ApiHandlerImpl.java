package com.autumn.gateway.starter.api.handler;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.IApiHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;
import com.autumn.gateway.api.plugin.core.invoker.EndpointInvoker;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import com.autumn.gateway.core.processor.chain.DefaultPluginChain;
import com.autumn.gateway.core.processor.provider.PluginChainProvider;
import com.autumn.gateway.starter.biz.service.IApiContextManagerService;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27:19:19
 */
@Slf4j
@Component
public class ApiHandlerImpl implements IApiHandler {

  @Resource private Api api;

  @Resource private PluginChainProvider pluginChainProvider;

  @Resource private ReactorHandlerManager reactorHandlerManager;

  // TODO 预留的服务端地址
  @Resource private EndpointInvoker endPointInvoker;

  @Resource private IApiContextManagerService apiContextManagerService;

  @Override
  public void handle(RoutingContext routingContext) {

    DefaultPluginChain defaultPluginChain = pluginChainProvider.create();

    SimpleExecutionContext simpleExecutionContext = new SimpleExecutionContext(routingContext, api);

    // 后台服务地址  //TODO 属于预留代码
    simpleExecutionContext
        .getAttributes()
        .put(SimpleExecutionContext.ATTR_INVOKER, endPointInvoker);

    // 这个只能代表执行完第一个插件 而不算执行完整个插件链路 所以不能在这计数
    defaultPluginChain.handle(simpleExecutionContext);
  }

  @Override
  public Reactable getReactable() {
    return api;
  }

  @PreDestroy
  public void destroy() {
    log.info("ApiHandlerImpl destroy...");
    // 移除映射
    reactorHandlerManager.remove(api);
    // 移除上下文
    apiContextManagerService.unRegister(api);
  }
}
