package com.autumn.gateway.core.plugin;

import com.autumn.gateway.api.plugin.core.AbstractPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.common.enums.ResultCode;
import com.autumn.gateway.common.pojo.builder.ResponseBuilder;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-08-19 16:40
 */
@Component
public class DefaultLastPlugin extends AbstractPlugin {
  public DefaultLastPlugin(PluginParam pluginParam) {
    super(pluginParam);
  }

  public DefaultLastPlugin() {
    super(new PluginParam());
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
  @Override
  protected void doExecute(SimpleExecutionContext simpleExecutionContext) {

    // TODO  没有响应处理插件而引入的临时代码
    if (!simpleExecutionContext.getRoutingContext().response().ended()) {

      // @updator qius 检查是否有异常未处理，直接进行响应
      ArrayList<Map<String, Object>> exList =
          (ArrayList<Map<String, Object>>)
              simpleExecutionContext
                  .getAttributes()
                  .get(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY);
      if (null != exList && exList.size() > 0) {
        simpleExecutionContext
            .getRoutingContext()
            .response()
            .end("{\"exceptionList\":" + Json.encode(exList) + "}");
        return;
      }

      if (simpleExecutionContext.getRespResultAfterRewrite() == null
          && simpleExecutionContext.getRespResult() == null) {
        simpleExecutionContext
            .getRoutingContext()
            .response()
            .end(ResponseBuilder.failStr(ResultCode.NOT_FIND));
        return;
      }

      if (simpleExecutionContext.getRespResultAfterRewrite() != null) {
        HttpServerResponse response = simpleExecutionContext.getRoutingContext().response();
        response.headers().addAll(simpleExecutionContext.getRespHeaders());
        response.headers().remove("Content-Length");
        response
            .headers()
            .add(
                "Content-Length", simpleExecutionContext.getRespResultAfterRewrite().length() + "");
        response.end(simpleExecutionContext.getRespResultAfterRewrite());
        return;
      }
      if (simpleExecutionContext.getRespResult() != null) {
        HttpServerResponse response = simpleExecutionContext.getRoutingContext().response();
        response.headers().addAll(simpleExecutionContext.getRespHeaders());
        response.headers().remove("Content-Length");
        response
            .headers()
            .add("Content-Length", simpleExecutionContext.getRespResult().length() + "");
        response.end(simpleExecutionContext.getRespResult());
        return;
      }
    }
  }
  /**
   * <获得插件类型>
   *
   * @return :
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/24 19:15
   */
  @Override
  public ApiPluginTypeEnum getPluginType() {
    return ApiPluginTypeEnum.RESPONSE;
  }

  /**
   * <如果上下文中存在错误是否继续执行>
   *
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:10
   */
  @Override
  public Boolean errorEncounteredContinue() {
    return Boolean.TRUE;
  }

  /**
   * <>
   *
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:48
   */
  @Override
  public String getPluginId() {
    return "defaultLast";
  }
}
