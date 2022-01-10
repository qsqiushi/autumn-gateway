package com.autumn.gateway.server.vertx.handler;

import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.common.enums.ResultCode;
import com.autumn.gateway.common.pojo.builder.ResponseBuilder;
import com.autumn.gateway.common.util.AutumnMimeTypeUtils;
import com.autumn.gateway.core.handler.IGlobalApiHandler;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import com.autumn.gateway.core.handler.UrlHandler;
import com.autumn.gateway.core.service.IListableRouterManagerService;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * <>
 *
 * @author qiushi
 * @since 2021/7/29 15:29
 */
@Slf4j
@Component
public class GlobalApiHandler implements IGlobalApiHandler {

  private static final String APPLICATION_GRPC = "application/grpc";

  @Resource private IApiRegisterService apiRegister;

  @Resource private ReactorHandlerManager reactorHandlerManager;

  @Resource private IListableRouterManagerService listableRouterManagerService;

  @Resource private Gson gson;

  @Override
  public void handle(RoutingContext routingContext) {

    ThreadContext.put("logid", UUID.randomUUID().toString());

    HttpServerRequest httpServerRequest = routingContext.request();

    List<UrlHandler> urlHandlers = listableRouterManagerService.listableRouters();

    for (UrlHandler urlHandler : urlHandlers) {

      if (httpServerRequest.method() == urlHandler.getMethod()
          && StringUtils.equals(httpServerRequest.path(), urlHandler.getUrl())) {

        urlHandler.handle(routingContext);
        return;
      }
    }

    // TODO
    if (isWebSocket(httpServerRequest)) {
      log.info("是长连接");

    } else if (httpServerRequest.version() == HttpVersion.HTTP_2) {
      if (APPLICATION_GRPC.equals(httpServerRequest.getHeader(HttpHeaders.CONTENT_TYPE))) {
        log.info("是grpc协议");
      } else {
        log.info("是http2协议");
      }
    } else {
      // uri 除去域名和端口
      // absoluteURI 全路径
      String path = httpServerRequest.absoluteURI();
      HttpMethod httpMethod = httpServerRequest.method();
      MultiMap headers = httpServerRequest.headers();
      log.info("请求路径为[{}],请求方法为[{}],", path, httpMethod);
      log.info("请求头[{}]", headers.toString().replaceAll("\r\n|\r|\n", ""));
      if (routingContext.getBody() != null) {
        log.info("请求体[{}]", routingContext.getBody().toString().replaceAll("\r\n|\r|\n", ""));
      } else {
        log.info("请求体为空");
      }
      Api api = apiRegister.getMatch(httpServerRequest);
      ReactorHandler reactorHandle = reactorHandlerManager.get(api);
      if (api == null || reactorHandle == null) {
        routingContext
            .response()
            .putHeader("Content-Type", AutumnMimeTypeUtils.APPLICATION_JSON_VALUE_UTF8)
            .end(ResponseBuilder.failStr(ResultCode.NOT_FIND));
        return;
      }
      reactorHandle.handle(routingContext);
    }
  }

  /**
   * We are only considering HTTP_1.x requests for now. There is a dedicated RFC to support
   * WebSockets over HTTP2: https://tools.ietf.org/html/rfc8441
   *
   * @param httpServerRequest
   * @return
   */
  private boolean isWebSocket(HttpServerRequest httpServerRequest) {
    String connectionHeader = httpServerRequest.getHeader(HttpHeaders.CONNECTION);
    String upgradeHeader = httpServerRequest.getHeader(HttpHeaders.UPGRADE);
    boolean isUpgrade = false;
    if (connectionHeader != null) {
      String[] connectionParts = connectionHeader.split(",");
      for (int i = 0; i < connectionParts.length && !isUpgrade; ++i) {
        isUpgrade = HttpHeaderValues.UPGRADE.contentEqualsIgnoreCase(connectionParts[i].trim());
      }
    }
    return (httpServerRequest.version() == HttpVersion.HTTP_1_0
            || httpServerRequest.version() == HttpVersion.HTTP_1_1)
        && httpServerRequest.method() == HttpMethod.GET
        && isUpgrade
        && HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(upgradeHeader);
  }
}
