package com.autumn.gateway.server.vertx.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;

import io.vertx.core.Vertx;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 16:25
 */
@Component
public class ServerBeanConfig {

	@Resource
	private IVertxManagerService vertxManagerService;

	@Bean
	public Vertx getVertx() {
		return vertxManagerService.getVertx();
	}
}
