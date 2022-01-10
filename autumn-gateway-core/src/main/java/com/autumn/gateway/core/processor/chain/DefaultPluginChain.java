package com.autumn.gateway.core.processor.chain;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.api.pojo.Reactable;
import io.vertx.core.Handler;
import org.apache.logging.log4j.ThreadContext;

import java.util.Iterator;
import java.util.List;

/**
 * @program qm-gateway
 * @description 插件链
 * @author qiushi
 * @since 2021-06-17 08:54
 */
public class DefaultPluginChain
    implements Handler<SimpleExecutionContext>, ReactorHandler<SimpleExecutionContext> {

  private final Iterator<IPlugin> pluginIterator;

  private final Api api;

  private Handler<SimpleExecutionContext> afterHandler;

  public DefaultPluginChain(List<IPlugin> plugins, Api api) {
    this.pluginIterator = plugins.iterator();
    this.api = api;
  }

  public boolean hasNext() {
    return pluginIterator.hasNext();
  }

  public IPlugin next() {
    return pluginIterator.next();
  }

  protected IPlugin next(IPlugin plugin) {
    return next();
  }

  @Override
  public Reactable getReactable() {
    return this.api;
  }

  public DefaultPluginChain afterHandler(Handler afterHandler) {
    this.afterHandler = afterHandler;
    return this;
  }

  /**
   * Something has happened, so handle it.
   *
   * @param simpleExecutionContext the event to handle
   */
  @Override
  public void handle(SimpleExecutionContext simpleExecutionContext) {
    doNext(simpleExecutionContext);
  }

  private void doNext(SimpleExecutionContext simpleExecutionContext) {

    if (pluginIterator != null && hasNext()) {

      IPlugin plugin = pluginIterator.next();

      plugin.execute(simpleExecutionContext, this);

      // 在 plugin.execute 中调用 handle方法
      // doNext(event, api);
    } else {
      if (afterHandler != null) {
        afterHandler.handle(simpleExecutionContext);
      }
      ThreadContext.clearAll();
    }
  }
}
