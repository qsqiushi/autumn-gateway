package com.autumn.gateway.core.plugin;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.autumn.gateway.api.plugin.core.AbstractPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.api.plugin.core.util.ReqRespUtil;
import com.autumn.gateway.common.constant.HttpConstants;
import com.autumn.gateway.common.enums.ResultCode;
import com.autumn.gateway.common.pojo.builder.ResponseBuilder;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;

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
   * @param ctx 上下文
   * @author qiushi
   * @updater qiushi
   * @since 2021/7/12 14:05
   */
  @Override
  protected void doExecute(SimpleExecutionContext ctx) {

    // 没有响应处理插件而引入的代码
    if (!ctx.getRoutingContext().response().ended()) {

      // @updater JiangLei 检查是否有异常未处理，直接进行响应
      ArrayList<Map<String, Object>> exList =
              (ArrayList<Map<String, Object>>)
                      ctx.getAttributes().get(SimpleExecutionContext.ATTR_PLUGIN_EXCEPTION_LIST_KEY);
      if (null != exList && !exList.isEmpty()) {
        ctx.getRoutingContext()
                .response()
                .putHeader(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON_VALUE_UTF8);

        // 返回响应
        ReqRespUtil.endResp(
                ctx,
                500,
                Json.encode(
                        ResponseBuilder.buildResponse(
                                ResultCode.SVR_INNER_ERROR.getCode(),
                                ResultCode.SVR_INNER_ERROR.getName(),
                                exList.get(0))));
        return;
      }

      if (ctx.getRespResult() == null) {
        ctx.getRoutingContext()
                .response()
                .putHeader(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON_VALUE_UTF8);

        // 返回响应
        ReqRespUtil.endResp(
                ctx, 500, ResponseBuilder.failStr(ResultCode.SVR_INNER_ERROR_NOT_RESULT));

        return;
      }

      HttpServerResponse response = ctx.getRoutingContext().response();
      response.headers().addAll(ctx.getRespHeaders());
      response.headers().remove(HttpConstants.CONTENT_LENGTH);
      // Parse Error: "Content-Length" and "Transfer-Encoding" can't be present in the response
      // headers together
      response.headers().remove(HttpConstants.TRANSFER_ENCODING);
      response
              .headers()
              .add(HttpConstants.CONTENT_LENGTH, ctx.getRespResult().getBytes().length + "");
      // 返回响应
      ReqRespUtil.endResp(ctx, 200, ctx.getRespResult());
    }
  }

  /**
   * <获得插件类型>
   *
   * @return :
   * @author qiushi
   * @updater qiushi
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
   * @updater qiushi
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
   * @updater qiushi
   * @since 2021/9/7 09:48
   */
  @Override
  public String getPluginId() {
    return "defaultLast";
  }
}
