package com.autumn.gateway.starter.api.handler;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.IApiHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import com.autumn.gateway.core.processor.chain.DefaultPluginChain;
import com.autumn.gateway.core.processor.provider.PluginChainProvider;
import com.autumn.gateway.service.IApiContextManagerService;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27:19:19
 */
@Slf4j
@Component
public class ApiHandlerImpl implements IApiHandler {

  @Autowired
  private Api api;

  @Autowired private PluginChainProvider pluginChainProvider;

  @Autowired private ReactorHandlerManager reactorHandlerManager;

  @Autowired private IApiContextManagerService apiContextManagerService;

  @Override
  public void handle(RoutingContext routingContext) {
    if (log.isDebugEnabled()) {
      log.debug("API处理过程...步骤[{}],[{}],API名称为[{}]", "2", "api处理器开始处理", api.getName());
    }
    // 创建插件链
    DefaultPluginChain defaultPluginChain = pluginChainProvider.create();
    // 构建上下文
    SimpleExecutionContext simpleExecutionContext = new SimpleExecutionContext(routingContext, api);
    // 这个只能代表执行完第一个插件 而不算执行完整个插件链路 所以不能在这计数
    defaultPluginChain.handle(simpleExecutionContext);
  }

  @Override
  public Api getReactable() {
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
