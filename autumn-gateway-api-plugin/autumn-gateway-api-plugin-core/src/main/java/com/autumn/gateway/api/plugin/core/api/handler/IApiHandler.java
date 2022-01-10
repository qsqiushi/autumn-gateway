package com.autumn.gateway.api.plugin.core.api.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-24 08:25
 */
public interface IApiHandler extends Handler<RoutingContext>, ReactorHandler<RoutingContext> {}
