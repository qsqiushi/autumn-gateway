package com.autumn.gateway.server.vertx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autumn.gateway.core.service.server.IServer;
import com.autumn.gateway.server.vertx.verticle.StartVerticle;

import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @program autumn
 * @description
 * @author qiushi
 * @since 2021-08-10:09:19
 */
@Slf4j
@Service
public class VertxServerImpl implements IServer {

	@Resource
	private Vertx vertx;

	@Value("${autumn.server.instances:16}")
	private Integer instances;

	private Boolean started = false;

	private List<String> verticleDeployIds;

	/**
	 * <f服务是否启动>
	 *
	 * @return : boolean
	 * @author qiushi
	 * @updator qiushi
	 * @since 2021/8/23 09:51
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	/**
	 * <启动服务>
	 *
	 * @return : void
	 * @author qiushi
	 * @updator qiushi
	 * @since 2021/8/10 09:17
	 */
	@Override
	public void start() {

		if (isStarted()) {
			log.info("server has bean started...");
			return;
		}
		// 启动
		DeploymentOptions deploymentOptions = new DeploymentOptions()
				.setInstances(instances)
				.setWorkerPoolSize(30)
				.setMaxWorkerExecuteTime(3000)
				.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

		vertx.deployVerticle(StartVerticle.class, deploymentOptions).onComplete(asyncResult -> {

			if (asyncResult.succeeded()) {
				log.info("asyncResult DeployId [{}]", asyncResult.result());

				if (verticleDeployIds == null) {
					verticleDeployIds = new ArrayList<>();
				}
				verticleDeployIds.add(asyncResult.result());
				started = true;
			} else {
				log.error("Deployment failed!", asyncResult.cause());
			}

		});

	}

	/**
	 * <停止服务>
	 *
	 * @author qiushi
	 * @updator qiushi
	 * @since 2021/8/10 09:17
	 */
	@Override
	public void stop() {

		if (!isStarted()) {
			log.info("server has bean stopped");
		}

		if (verticleDeployIds != null) {
			List<Future> futures = verticleDeployIds.stream().map(deployId -> Future.future(r -> {
				vertx.undeploy(deployId);
			})).collect(Collectors.toList());

			CompositeFuture.all(futures).onComplete(asyncResult -> {

				if (asyncResult.succeeded()) {
					started = false;
				} else {
					log.error("关闭服务失败", asyncResult.cause());
				}
			});

		}

	}
}
