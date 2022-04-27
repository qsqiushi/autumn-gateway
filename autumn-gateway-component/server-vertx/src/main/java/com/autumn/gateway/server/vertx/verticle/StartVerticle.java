package com.autumn.gateway.server.vertx.verticle;

import com.autumn.gateway.common.enums.ResultCode;
import com.autumn.gateway.common.pojo.builder.ResponseBuilder;
import com.autumn.gateway.common.util.AutumnMimeTypeUtils;
import com.autumn.gateway.core.handler.IGlobalApiHandler;
import com.autumn.gateway.server.vertx.service.impl.AutumnBodyHandlerImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program qm-gateway
 * @description
 * @author qiushi
 * @since 2021-06-05:13:25
 */
@Slf4j
@Component
public class StartVerticle extends AbstractVerticle {

  @Value("${autumn.server.port:80}")
  private Integer port;

  @Resource private IGlobalApiHandler globalApiHandler;

  @Resource private HttpServer httpServer;

  @Override
  public void start() throws Exception {
    super.start();

    Router router = Router.router(vertx);

    // router.get("/hystrix-metrics").handler(HystrixMetricHandler.create(vertx)) 熔断的支持

    router.route().handler(new AutumnBodyHandlerImpl());
    // defaultApiHandler 处理网关核心逻辑
    // 添加例外 在globalApiHandler 中处理
    router
        .route("/*")
        // .handler(BodyHandler.create())
        .handler(globalApiHandler::handle)
        .failureHandler(
            routingContext -> {
              Throwable throwable = routingContext.failure();
              if (!routingContext.response().ended()) {
                routingContext
                    .response()
                    .putHeader("Content-Type", AutumnMimeTypeUtils.APPLICATION_JSON_VALUE_UTF8)
                    .end(
                        ResponseBuilder.failStr(
                            ResultCode.BAD_REQUEST.getCode(), throwable.getMessage()));
              }
            });
    httpServer
        .requestHandler(router::handle)
        .exceptionHandler(
            ex -> {
              log.error("处理时出现异常", ex);
            })
        .listen(
            port,
            server -> {
              if (server.succeeded()) {
                log.info("vertx server start  on port [{}]", port);
              } else {
                server.cause().printStackTrace();
              }
            });
  }
}
