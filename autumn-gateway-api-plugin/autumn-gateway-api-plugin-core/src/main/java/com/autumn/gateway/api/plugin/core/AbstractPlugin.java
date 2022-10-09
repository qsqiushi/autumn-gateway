package com.autumn.gateway.api.plugin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.common.util.CommonUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-24 19:28
 */
@Data
@Slf4j
public abstract class AbstractPlugin implements IPlugin {

  private PluginParam pluginParam;

  protected AbstractPlugin(PluginParam pluginParam) {
    this.pluginParam = pluginParam;
  }

  public PluginParam getPluginParam() {
    return this.pluginParam;
  }

  /**
   * <执行插件>
   *
   * @param simpleExecutionContext 上下文
   * @author qiushi
   * @updater qiushi
   * @since 2021/7/12 14:05
   */
  protected abstract void doExecute(SimpleExecutionContext simpleExecutionContext);

  /**
   * <执行>
   *
   * @param simpleExecutionContext 上下文
   * @param handler 处理器
   * @author qiushi
   * @updater qiushi
   * @since 2021/6/5 14:54
   */
  @Override
  public void execute(
          SimpleExecutionContext simpleExecutionContext,
          ReactorHandler<SimpleExecutionContext, Api> handler) {

    Boolean doExecute = CommonUtil.getBoolean();
    if (Boolean.TRUE.equals(simpleExecutionContext.getHasError())
            && Boolean.FALSE.equals(errorEncounteredContinue())) {
      doExecute = false;
    }

    if (Boolean.TRUE.equals(doExecute)) {
      try {
        Api api = handler.getReactable();
        if (log.isDebugEnabled()) {
          log.debug(
                  "api[{}]的[{}]插件的策略[{}]",
                  api.getUrl(),
                  getPluginId(),
                  getPluginParam().getJsonParam());
        }
        doExecute(simpleExecutionContext);
      } catch (Exception e) {
        throwPluginException(simpleExecutionContext, e);
        log.error("异常", e);
      }
    }

    handler.handle(simpleExecutionContext);
  }

  /**
   * 由实现的子类当抛出异常时进行调用，并告知异常的严重程度，是否允许下一个插件进行正常业务处理
   *
   * @param exception 异常
   * @param ctx 上下文
   * @author JiangLei
   * @date 2021/8/24 10:16
   */
  public final void throwPluginException(SimpleExecutionContext ctx, Throwable exception) {
    // 放置于context中
    ctx.errorEncountered();
    log.error("an error has occurred in [{}] ", this.getClass().getSimpleName(), exception);
    List<Map<String, Object>> exceptionList =
            (ArrayList<Map<String, Object>>)
                    ctx.getAttributes().get(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY);

    HashMap<String, Object> exMap = new HashMap<>(10);
    exMap.put(getPluginId(), exception.getMessage());

    if (null == exceptionList) {
      ArrayList<Map<String, Object>> list = new ArrayList<>();
      list.add(exMap);
      ctx.getAttributes().put(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY, list);
    } else {
      exceptionList.add(exMap);
    }
  }
}
