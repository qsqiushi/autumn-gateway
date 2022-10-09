package com.autumn.gateway.api.plugin.proxy.http;

import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import com.autumn.gateway.api.plugin.core.AbstractPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;
import com.autumn.gateway.api.plugin.core.pojo.AutumnCircuitBreaker;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import com.autumn.gateway.common.constant.HttpConstants;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.multipart.MultipartForm;
import lombok.extern.slf4j.Slf4j;

/**
 * @program agw
 * @description
 * @author qiushi
 * @since 2021-07-07:09:18
 */
@Slf4j
public class HttpProxyPlugin extends AbstractPlugin {

	private static final Map<Vertx, WebClient> WEB_CLIENTS = new ConcurrentHashMap<>();

	public static final String PROXY_ERROR = "代理服务出现错误";

	private final IFailInvokerManagerService failInvokerManagerService;

	public HttpProxyPlugin(
			PluginParam pluginParam, IFailInvokerManagerService failInvokerManagerService) {
		super(pluginParam);
		this.failInvokerManagerService = failInvokerManagerService;
	}

	/**
	 * <执行插件>
	 *
	 * @param simpleExecutionContext
	 *            上下文
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/7/12 14:05
	 */
	@Override
	protected void doExecute(SimpleExecutionContext simpleExecutionContext) {
		// DO NOTHING
	}

	/**
	 * <获得插件类型>
	 *
	 * @return : com.qm.plugin.api.com.qm.qos.enums.ApiPluginTypeEnum
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/6/24 19:15
	 */
	@Override
	public ApiPluginTypeEnum getPluginType() {
		return ApiPluginTypeEnum.PROXY;
	}

	/** 去异味 高复杂度execute方法 */
	private void logDebugHttpProxyPluginStart(String msg) {
		if (log.isDebugEnabled()) {
			log.debug(msg);
		}
	}

	/***
	 * 去异味，高复杂度，拆分execute()方法
	 *
	 * @param ctx
	 *            上下文
	 * @param handler
	 *            处理器
	 * @author 李鹤
	 * @since 2022/7/6 9:48
	 */
	@Override
	public void execute(
			SimpleExecutionContext ctx, ReactorHandler<SimpleExecutionContext, Api> handler) {
		logDebugHttpProxyPluginStart("HttpProxyPlugin处理请求开始...");

		ctx.getAttributes().put(SimpleExecutionContext.PROXY_CLASS_KEY, this.getClass().getName());
		ctx.getAttributes()
				.put(SimpleExecutionContext.PROXY_START_TIME_KEY, System.currentTimeMillis());

		// 判断出错时是否还需要处理
		if (Boolean.TRUE.equals(ctx.getHasError())
				&& Boolean.FALSE.equals(errorEncounteredContinue())) {
			doNext(ctx, handler);
			return;
		}
		Api api = handler.getReactable();
		if (log.isDebugEnabled()) {
			log.debug(
					"api[{}]的[{}]插件的策略[{}]",
					api.getUrl(),
					this.getPluginId(),
					this.getPluginParam().getJsonParam());
		}

		AutumnCircuitBreaker agwCircuitBreaker = failInvokerManagerService.getCircuitBreaker(api);
		Vertx vertx = Vertx.currentContext().owner();
		// Grab an instance of the HTTP client
		final WebClient webClient = WEB_CLIENTS.computeIfAbsent(vertx, createWebClient(vertx));

		// 获取query参数
		MultiMap queryParameters = ctx.getProxyQueryParameters();
		// 获取访问路径
		String urlStr = ctx.getAttributes().get(SimpleExecutionContext.ATTR_HTTP_URL).toString();
		// 构造URI 如此处理是解决 后台服务地址为带query参数的地址
		URI uri;
		try {
			uri = new URI(urlStr);
			URIBuilder uriBuilder = new URIBuilder(uri);
			if (queryParameters != null) {
				for (Map.Entry<String, String> entry : queryParameters.entries()) {
					uriBuilder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			uri = uriBuilder.build();
		} catch (Exception ex) {
			log.error(PROXY_ERROR, ex);
			super.throwPluginException(ctx, ex);
			ctx.setProxyResult(ex.toString());
			doNext(ctx, handler);
			return;
		}
		Long timeout = 2000L;

		if (api.getTimeOut() != null) {
			timeout = api.getTimeOut();
		}

		// 获得后台服务方法
		HttpMethod httpMethod = HttpMethod.valueOf(api.getServiceHttpMethod().toUpperCase(Locale.ROOT));
		String any = "ANY";
		if (StringUtils.equals(any, api.getServiceHttpMethod().toUpperCase(Locale.ROOT))) {
			httpMethod = ctx.getRoutingContext().request().method();
		}

		// 请求头
		MultiMap headers = ctx.getProxyHeaders();
		// 新的请求
		HttpRequest<Buffer> httpRequest = webClient.requestAbs(httpMethod, uri.toString()).timeout(timeout);

		httpRequest.ssl(urlStr.toLowerCase(Locale.ROOT).startsWith("https"));

		if (headers != null) {
			httpRequest.putHeaders(headers);
		}

		if (log.isDebugEnabled()) {
			log.debug("api[{}]的后台请求的实际地址为[{}]", api.getUrl(), uri.toString());
		}

		ctx.getAttributes().put(SimpleExecutionContext.PROXY_REAL_URL_KEY, uri.toString());

		if (agwCircuitBreaker == null) {
			// 直接调用服务端
			send(ctx, handler, httpRequest);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("熔断器当前的状态是[{}]", agwCircuitBreaker.state());
			}
			sendCircuitBreaker(ctx, handler, agwCircuitBreaker, httpRequest);
		}
	}

	/**
	 * sendCircuitBreaker 去异味，复杂度
	 *
	 * @author 李鹤
	 * @time 2022/7/4 13:52
	 */
	public void sendMsg(
			HttpRequest<Buffer> httpRequest,
			HttpMethod httpMethod,
			SimpleExecutionContext ctx,
			Handler<AsyncResult<HttpResponse<Buffer>>> asyncHandler) {
		// 未处理的请求体
		String proxyRequestBody = ctx.getProxyRequestBody();
		// 处理后的请求体
		String proxyRequestBodyAfterRewrite = ctx.getProxyRequestBody();
		if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
				&& StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
				&& ctx.getProxyHeaders()
				.get(HttpConstants.CONTENT_TYPE)
				.toLowerCase(Locale.ROOT)
				.contains(HttpConstants.MULTIPART_FORM_DATA)) {
			log.info("文件上传表单调用");
			// 每次上传文件 请求头都会有变化，需要重新设置 multipart/form-data; boundary=d98b93ab32fc86e8
			httpRequest.putHeader(HttpConstants.CONTENT_TYPE, HttpConstants.MULTIPART_FORM_DATA);
			MultipartForm multipartForm = getFormDataParts(ctx);
			httpRequest.sendMultipartForm(multipartForm, asyncHandler);
			return;
		}

		if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
				&& StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
				&& ctx.getProxyHeaders()
				.get(HttpConstants.CONTENT_TYPE)
				.toLowerCase(Locale.ROOT)
				.contains(HttpConstants.APPLICATION_X_WWW_FORM_URLENCODED)) {
			log.info("表单调用");
			httpRequest.sendForm(ctx.getProxyFormAttributes(), asyncHandler);
			return;
		}

		if (StringUtils.isNotEmpty(proxyRequestBodyAfterRewrite)) {
			httpRequest.sendBuffer(Buffer.buffer(proxyRequestBodyAfterRewrite), asyncHandler);
		} else if (proxyRequestBody != null && proxyRequestBody.length() > 0) {
			httpRequest.sendBuffer(Buffer.buffer(proxyRequestBody), asyncHandler);
		} else {
			httpRequest.send(asyncHandler);
		}
	}

	/**
	 * < 熔断器监察发送>
	 *
	 * @param ctx
	 *            上下文
	 * @param handler
	 *            处理器
	 * @param agwCircuitBreaker
	 *            熔断器
	 * @param httpRequest
	 *            请求
	 * @author qiushi
	 * @updater qiushi
	 * @since 2022/2/19 14:51
	 *        <p>
	 *        去重复异味 sendMsg(httpRequest,httpMethod,ctx,asyncHandler); if
	 *        (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name()) &&
	 *        StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
	 *        &&
	 *        ctx.getProxyHeaders() .get(HttpConstants.CONTENT_TYPE)
	 *        .toLowerCase(Locale.ROOT)
	 *        .contains(HttpConstants.MULTIPART_FORM_DATA)) { log.info("文件上传表单调用");
	 *        // 每次上传文件
	 *        请求头都会有变化，需要重新设置 multipart/form-data; boundary=d98b93ab32fc86e8
	 *        httpRequest.putHeader(
	 *        HttpConstants.CONTENT_TYPE, HttpConstants.MULTIPART_FORM_DATA);
	 *        MultipartForm multipartForm
	 *        = getFormDataParts(ctx); httpRequest.sendMultipartForm(multipartForm,
	 *        asyncHandler);
	 *        return; } if (StringUtils.equals(HttpMethod.POST.name(),
	 *        httpMethod.name()) &&
	 *        StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
	 *        &&
	 *        ctx.getProxyHeaders() .get(HttpConstants.CONTENT_TYPE)
	 *        .toLowerCase(Locale.ROOT)
	 *        .contains(HttpConstants.APPLICATION_X_WWW_FORM_URLENCODED)) {
	 *        log.info("表单调用");
	 *        httpRequest.sendForm(ctx.getProxyFormAttributes(), asyncHandler);
	 *        return; } if
	 *        (StringUtils.isNotEmpty(proxyRequestBodyAfterRewrite)) {
	 *        httpRequest.sendBuffer(Buffer.buffer(proxyRequestBodyAfterRewrite),
	 *        asyncHandler); } else
	 *        if (proxyRequestBody != null && proxyRequestBody.length() > 0) {
	 *        httpRequest.sendBuffer(Buffer.buffer(proxyRequestBody), asyncHandler);
	 *        } else {
	 *        httpRequest.send(asyncHandler); }
	 */
	private void sendCircuitBreaker(
			SimpleExecutionContext ctx,
			ReactorHandler<SimpleExecutionContext, Api> handler,
			AutumnCircuitBreaker agwCircuitBreaker,
			HttpRequest<Buffer> httpRequest) {

		Api api = handler.getReactable();
		// 获得后台服务方法
		HttpMethod httpMethod = HttpMethod.valueOf(api.getServiceHttpMethod().toUpperCase(Locale.ROOT));
		// 如果有熔断器
		agwCircuitBreaker
				.<Void>execute(
						promise -> {
							Handler<AsyncResult<HttpResponse<Buffer>>> asyncHandler = httpResponseAsyncResult -> handleRespAsync(
									ctx, promise, httpResponseAsyncResult);
							/** 去重复异味，详见本方法注释 */
							sendMsg(httpRequest, httpMethod, ctx, asyncHandler);
						})
				.onComplete(
						ar -> {
							if (ar.succeeded()) {
								if (log.isDebugEnabled()) {
									log.debug("API[{}]后台服务访问正常", api.getName());
								}
							} else {
								log.error("API[{}]后台服务访问出现异常", api.getName(), ar.cause());
								throwPluginException(ctx, ar.cause());
							}
							doNext(ctx, handler);
						});
	}

	private void doNext(
			SimpleExecutionContext ctx, ReactorHandler<SimpleExecutionContext, Api> handler) {

		long startTime = (Long) ctx.getAttributes().get(SimpleExecutionContext.PROXY_START_TIME_KEY);

		ctx.getAttributes().put(SimpleExecutionContext.PROXY_END_TIME_KEY, System.currentTimeMillis());

		if (log.isDebugEnabled()) {
			log.debug("HttpProxyPlugin处理请求耗时[{}]ms", System.currentTimeMillis() - startTime);
			log.debug("HttpProxyPlugin处理请求结束");
		}
		handler.handle(ctx);
	}

	/**
	 * <>
	 *
	 * @param ctx
	 *            上下文
	 * @param chainHandler
	 *            处理器
	 * @param httpRequest
	 *            请求
	 * @author qiushi
	 * @updater qiushi
	 * @since 2022/1/27 10:00
	 */
	private void send(
			SimpleExecutionContext ctx,
			ReactorHandler<SimpleExecutionContext, Api> chainHandler,
			HttpRequest<Buffer> httpRequest) {
		// API信息
		Api api = chainHandler.getReactable();
		// 获得后台服务方法
		HttpMethod httpMethod = HttpMethod.valueOf(api.getServiceHttpMethod().toUpperCase(Locale.ROOT));
		// 未处理的请求体
		String proxyRequestBody = ctx.getProxyRequestBody();
		// 没有熔断器
		Future<HttpResponse<Buffer>> future;

		if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
				&& StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
				&& ctx.getProxyHeaders()
				.get(HttpConstants.CONTENT_TYPE)
				.toLowerCase(Locale.ROOT)
				.contains(HttpConstants.MULTIPART_FORM_DATA)) {
			MultipartForm multipartForm = getFormDataParts(ctx);
			httpRequest.putHeader(HttpConstants.CONTENT_TYPE, HttpConstants.MULTIPART_FORM_DATA);
			// 文件上传
			future = httpRequest.sendMultipartForm(multipartForm);
		} else if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
				&& StringUtils.isNotBlank(ctx.getProxyHeaders().get(HttpConstants.CONTENT_TYPE))
				&& ctx.getProxyHeaders()
				.get(HttpConstants.CONTENT_TYPE)
				.toLowerCase(Locale.ROOT)
				.contains(HttpConstants.APPLICATION_X_WWW_FORM_URLENCODED)) {
			// 表单提交
			future = httpRequest.sendForm(ctx.getProxyFormAttributes());
		} else {
			if (StringUtils.isNotEmpty(proxyRequestBody)) {
				// 如果请求数据被处理过
				future = httpRequest.sendBuffer(Buffer.buffer(proxyRequestBody));
			} else {
				// 如果都为空
				future = httpRequest.send();
			}
		}

		future.onComplete(asyncResult -> {
			if (asyncResult.succeeded()) {
				// 设置服务端响应
				ctx.setProxyResult(asyncResult.result().bodyAsString());
				// 设置网关响应结果
				ctx.setRespResult(asyncResult.result().bodyAsString());
				// 设置请求头
				ctx.setProxyRespHeaders(asyncResult.result().headers());
				ctx.setRespHeaders(asyncResult.result().headers());
				// 设置状态
				ctx.setProxyStatusCode(asyncResult.result().statusCode());
				// 设置cookies
				ctx.setProxyCookies(asyncResult.result().cookies());
				// 有结果后交给插件链处理
				doNext(ctx, chainHandler);
			} else {
				log.error(PROXY_ERROR, asyncResult.cause());
				// 设置服务端响应
				ctx.setProxyResult(asyncResult.cause().toString());
				// 设置网关响应结果
				ctx.setRespResult(asyncResult.cause().toString());
				// @updater JiangLei 继续向下执行，并将异常记录 */
				throwPluginException(ctx, asyncResult.cause());
				if (asyncResult.result() != null) {
					// 设置状态
					ctx.setProxyStatusCode(asyncResult.result().statusCode());
				}
				// 有结果后交给插件链处理
				doNext(ctx, chainHandler);
			}
		});
	}

	/**
	 * <构造表单参数>
	 *
	 * @param ctx
	 *            上下文
	 * @return io.vertx.ext.web.multipart.MultipartForm
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/9/27 10:52
	 */
	private MultipartForm getFormDataParts(SimpleExecutionContext ctx) {

		MultipartForm multipartForm = MultipartForm.create();
		if (ctx.getProxyFormAttributes() != null && !ctx.getProxyFormAttributes().isEmpty()) {
			ctx.getProxyFormAttributes()
					.forEach(multiMap -> multipartForm.attribute(multiMap.getKey(), multiMap.getValue()));
		}
		if (CollectionUtils.isNotEmpty(ctx.getProxyUploadFiles())) {
			ctx.getProxyUploadFiles()
					.forEach(
							agwFileUpload -> multipartForm.textFileUpload(
									agwFileUpload.getName(),
									agwFileUpload.getFileUpload().fileName(),
									agwFileUpload.getFileUpload().uploadedFileName(),
									agwFileUpload.getFileUpload().contentType()));
		}

		return multipartForm;
	}

	/**
	 * <>
	 *
	 * @param ctx
	 *            上下文
	 * @param promise
	 *            promise
	 * @param httpResponseAsyncResult
	 *            异步处理
	 * @author qiushi
	 * @updater qiushi
	 * @since 2022/1/27 13:26
	 */
	private void handleRespAsync(
			SimpleExecutionContext ctx,
			io.vertx.core.Promise<Void> promise,
			io.vertx.core.AsyncResult<HttpResponse<Buffer>> httpResponseAsyncResult) {

		if (httpResponseAsyncResult.result() == null || httpResponseAsyncResult.cause() != null) {
			promise.fail(httpResponseAsyncResult.cause());
			log.error(PROXY_ERROR, httpResponseAsyncResult.cause());
			// 设置服务端响应
			ctx.setProxyResult(httpResponseAsyncResult.cause().toString());
			if (httpResponseAsyncResult.result() != null) {
				// 设置服务端响应码
				ctx.setProxyStatusCode(httpResponseAsyncResult.result().statusCode());
			}
			// 设置网关响应结果
			ctx.setRespResult(httpResponseAsyncResult.cause().toString());
			// @updater JiangLei 继续向下执行，并将异常记录
			super.throwPluginException(ctx, httpResponseAsyncResult.cause());

		} else {

			promise.complete();
			// 设置服务端响应
			ctx.setProxyResult(httpResponseAsyncResult.result().bodyAsString());
			// 设置网关响应结果
			ctx.setRespResult(httpResponseAsyncResult.result().bodyAsString());

			ctx.setProxyRespHeaders(httpResponseAsyncResult.result().headers());

			ctx.setRespHeaders(httpResponseAsyncResult.result().headers());

			ctx.setProxyStatusCode(httpResponseAsyncResult.result().statusCode());

			ctx.setProxyCookies(httpResponseAsyncResult.result().cookies());
		}
	}

	private Function<? super Vertx, ? extends WebClient> createWebClient(Vertx vertx) {

		WebClientOptions webClientOptions = new WebClientOptions();

		webClientOptions.setConnectTimeout(3000);

		// 设置连接的最大池大小
		webClientOptions.setMaxPoolSize(5);

		// 设置等待队列中允许的最大请求数，任何超出最大大小的请求都将导致 ConnectionPoolTooBusyException。
		webClientOptions.setMaxWaitQueueSize(-1);

		webClientOptions.setIdleTimeout(5000);

		webClientOptions.setTrustAll(true).setVerifyHost(false);

		return context -> WebClient.create(vertx, webClientOptions);
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
		return Boolean.FALSE;
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
		return "httpProxy";
	}
}
