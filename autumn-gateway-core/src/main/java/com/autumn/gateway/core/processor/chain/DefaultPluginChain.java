package com.autumn.gateway.core.processor.chain;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.common.constant.ReqConstants;

import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;

/**
 * @program qm-gateway
 * @description 插件链
 * @author qiushi
 * @since 2021-06-17 08:54
 */
@Slf4j
public class DefaultPluginChain
		implements Handler<SimpleExecutionContext>, ReactorHandler<SimpleExecutionContext, Api> {

	private final Iterator<IPlugin> pluginIterator;

	private final Api api;

	private Handler<SimpleExecutionContext> afterHandler;

	private String current;

	private Long start;

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

	@Override
	public Api getReactable() {
		return this.api;
	}

	public DefaultPluginChain afterHandler(Handler<SimpleExecutionContext> afterHandler) {
		this.afterHandler = afterHandler;
		return this;
	}

	/**
	 * Something has happened, so handle it.
	 *
	 * @param simpleExecutionContext
	 *            the event to handle
	 */
	@Override
	public void handle(SimpleExecutionContext simpleExecutionContext) {
		doNext(simpleExecutionContext);
	}

	private void doNext(SimpleExecutionContext ctx) {

		if (pluginIterator != null && hasNext()) {

			recordPluginTakes(ctx);
			// 获取插件
			IPlugin plugin = pluginIterator.next();
			current = plugin.getPluginId();
			// 开始时间
			start = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("[{}]插件开始处理[{}]", current, api.getName());
			}
			plugin.execute(ctx, this);

		} else {

			recordPluginTakes(ctx);
			if (afterHandler != null) {
				afterHandler.handle(ctx);
			}
			recordCost(ctx);
		}
	}

	private void recordPluginTakes(SimpleExecutionContext ctx) {
		if (StringUtils.isNotBlank(current) && start != null) {
			long takes = System.currentTimeMillis() - start;
			if (log.isDebugEnabled()) {
				log.debug("[{}]插件处理[{}]完毕,耗时[{}]", current, api.getName(), takes);
			}
			ctx.getAttributes().put(SimpleExecutionContext.ATTR_TAKE_TIMES + current, takes);
		}
	}

	/**
	 * 修复异味
	 * 
	 * @param ctx
	 * @author 李鹤
	 * @time 2022/7/1 13:06
	 *       long start =
	 *       Long.parseLong(ThreadContext.get(ReqConstants.START_TIME));
	 *       long takes = System.currentTimeMillis() - start;
	 */
	public void recordCost(SimpleExecutionContext ctx) {
		if (StringUtils.isNumeric(ThreadContext.get(ReqConstants.START_TIME))) {
			long startTime = Long.parseLong(ThreadContext.get(ReqConstants.START_TIME));
			long takes = System.currentTimeMillis() - startTime;
			if (log.isDebugEnabled()) {
				log.debug("API[{}]整个请求耗时[{}]ms", ctx.getRoutingContext().request().absoluteURI(), takes);
			}
			ctx.getAttributes()
					.put(SimpleExecutionContext.ATTR_TAKE_TIMES + "all", takes);
			ThreadContext.clearAll();
		}
	}
}
