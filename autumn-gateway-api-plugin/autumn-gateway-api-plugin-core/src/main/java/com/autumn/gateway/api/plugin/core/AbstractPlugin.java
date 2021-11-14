package com.autumn.gateway.api.plugin.core;

import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-24:19:28
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
   * @param simpleExecutionContext
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/12 14:05
   */
  protected abstract void doExecute(SimpleExecutionContext simpleExecutionContext);

  /**
   * <执行>
   *
   * @param simpleExecutionContext
   * @param handler
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/5 14:54
   */
  @Override
  public void execute(SimpleExecutionContext simpleExecutionContext, ReactorHandler handler) {
    log.info("[{}] plugin start work ...", this.getClass().getSimpleName());
    long start = System.currentTimeMillis();
    Boolean doExecute = true;
    if (simpleExecutionContext.getHasError() && !errorEncounteredContinue()) {
      doExecute = false;
    }

    if (doExecute) {
      try {
        doExecute(simpleExecutionContext);
      } catch (Exception e) {
        throwPluginException(simpleExecutionContext, e);
        e.printStackTrace();
      }
    }

    long takes = System.currentTimeMillis() - start;

    log.info("[{}] plugin takes[{}] ms", this.getClass().getSimpleName(), takes);

    simpleExecutionContext
        .getAttributes()
        .put(SimpleExecutionContext.ATTR_TAKE_TIMES + this.getClass().getSimpleName(), takes);

    handler.handle(simpleExecutionContext);
  }

  /**
   * 由实现的子类当抛出异常时进行调用，并告知异常的严重程度，是否允许下一个插件进行正常业务处理
   *
   * @return void
   * @param: exception
   * @param: pluginName 插件名称用于记录日志
   * @param: canDoNext true 正常处理，false为下一个插件不进行业务处理
   * @param: simpleExecutionContext
   * @author qius
   * @date 2021/8/24 10:16
   */
  public final void throwPluginException(
      SimpleExecutionContext simpleExecutionContext, Throwable exception) {
    // 放置于context中

    simpleExecutionContext.errorEncountered();

    log.error("an error has occurred in [{}] ", this.getClass().getSimpleName(), exception);

    List<Map<String, Object>> exceptionList =
        (ArrayList<Map<String, Object>>)
            simpleExecutionContext
                .getAttributes()
                .get(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY);

    HashMap<String, Object> exMap = new HashMap<>();
    exMap.put(getPluginId(), exception);

    if (null == exceptionList) {
      ArrayList<Map<String, Object>> list = new ArrayList<>();
      list.add(exMap);
      simpleExecutionContext
          .getAttributes()
          .put(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY, list);
    } else {
      exceptionList.add(exMap);
    }
  }
}
