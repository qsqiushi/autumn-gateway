package com.autumn.gateway.api.plugin.proxy.http;

import com.autumn.gateway.api.plugin.core.AbstractPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;
import com.autumn.gateway.api.plugin.core.pojo.AutumnCircuitBreaker;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.multipart.MultipartForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @program agw
 * @description
 * @author qiushi
 * @since 2021-07-07:09:18
 */
@Slf4j
public class HttpProxyPlugin extends AbstractPlugin {

  private static final Map<Vertx, WebClient> WEB_CLIENTS = new ConcurrentHashMap<>();

  private IFailInvokerManagerService failInvokerManagerService;

  public HttpProxyPlugin(
      PluginParam pluginParam, IFailInvokerManagerService failInvokerManagerService) {
    super(pluginParam);

    this.failInvokerManagerService = failInvokerManagerService;
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
    // DO NOTHING
  }

  /**
   * <获得插件类型>
   *
   * @return : com.qm.plugin.api.com.qm.qos.enums.ApiPluginTypeEnum
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/24 19:15
   */
  @Override
  public ApiPluginTypeEnum getPluginType() {
    return ApiPluginTypeEnum.PROXY;
  }

  @Override
  public void execute(SimpleExecutionContext simpleExecutionContext, ReactorHandler handler) {

    if (simpleExecutionContext.getHasError() && !errorEncounteredContinue()) {
      handler.handle(simpleExecutionContext);
      return;
    }

    Api api = (Api) handler.getReactable();

    AutumnCircuitBreaker agwCircuitBreaker = failInvokerManagerService.getCircuitBreaker(api);

    Vertx vertx = Vertx.currentContext().owner();

    long finalStart = System.currentTimeMillis();

    // Grab an instance of the HTTP client
    final WebClient webClient = WEB_CLIENTS.computeIfAbsent(vertx, createWebClient(vertx));

    // 获取query参数
    MultiMap queryParameters = simpleExecutionContext.getProxyQueryParameters();

    // 获取访问路径
    String urlStr =
        simpleExecutionContext.getAttributes().get(SimpleExecutionContext.ATTR_HTTP_URL).toString();

    // 构造URI   如此处理是解决 后台服务地址为带query参数的地址
    URI uri;

    try {
      uri = new URI(urlStr);
      URIBuilder uriBuilder = new URIBuilder(uri);

      if (queryParameters != null) {
        Iterator<Map.Entry<String, String>> iterator = queryParameters.entries().iterator();
        while (iterator.hasNext()) {

          Map.Entry<String, String> entry = iterator.next();

          String key = entry.getKey();

          String value = entry.getValue();

          uriBuilder.addParameter(key, value);
        }
      }
      uri = uriBuilder.build();
    } catch (Exception ex) {
      log.error("代理服务出现错误", ex);
      super.throwPluginException(simpleExecutionContext, ex);
      simpleExecutionContext.setProxyResult(Buffer.buffer(ex.toString()));
      handler.handle(simpleExecutionContext);
      return;
    }

    Integer timeout = null;
    try {
      timeout = Integer.parseInt(api.getTimeOut());
    } catch (Exception ex) {
      log.error("api 超时设置为[{}}],解析错误", api.getTimeOut(), ex);
      timeout = 2000;
    }

    // 获得后台服务方法
    HttpMethod httpMethod = HttpMethod.valueOf(api.getServiceHttpMethod().toUpperCase(Locale.ROOT));

    /** 未处理的请求体 */
    Buffer proxyRequestBody = simpleExecutionContext.getProxyRequestBody();
    /** 处理后的请求体 */
    String proxyRequestBodyAfterRewrite = simpleExecutionContext.getProxyRequestBodyAfterRewrite();
    /** 请求头 */
    MultiMap headers = simpleExecutionContext.getProxyHeaders();

    HttpRequest<Buffer> httpRequest =
        webClient.requestAbs(httpMethod, uri.toString()).timeout(timeout);

    if (headers != null) {
      httpRequest.putHeaders(headers);
    }

    if (agwCircuitBreaker == null) {

      send(simpleExecutionContext, handler, httpRequest);

    } else {
      log.info("agwCircuitBreaker state [{}]", agwCircuitBreaker.state());

      // 如果有熔断器
      agwCircuitBreaker
          .<Void>execute(
              promise -> {
                Handler<AsyncResult<HttpResponse<Buffer>>> asyncHandler =
                    httpResponseAsyncResult -> {
                      handleRespAsync(
                          simpleExecutionContext, handler, promise, httpResponseAsyncResult);
                    };

                if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
                    && simpleExecutionContext
                        .getHeaders()
                        .get("Content-Type")
                        .toLowerCase(Locale.ROOT)
                        .contains("multipart/form-data")) {
                  MultipartForm multipartForm = getFormDataParts(simpleExecutionContext);
                  httpRequest.sendMultipartForm(multipartForm, asyncHandler);
                }

                if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
                    && simpleExecutionContext
                        .getHeaders()
                        .get("Content-Type")
                        .toLowerCase(Locale.ROOT)
                        .contains("application/x-www-form-urlencoded")) {

                  httpRequest.sendForm(
                      simpleExecutionContext.getProxyFormAttributes(), asyncHandler);
                }

                if (StringUtils.isNotEmpty(proxyRequestBodyAfterRewrite)) {
                  httpRequest.sendBuffer(Buffer.buffer(proxyRequestBodyAfterRewrite), asyncHandler);

                } else if (proxyRequestBody != null && proxyRequestBody.length() > 0) {
                  httpRequest.sendBuffer(proxyRequestBody, asyncHandler);
                } else {
                  httpRequest.send(asyncHandler);
                }
              })
          .onComplete(
              ar -> {
                if (ar.succeeded()) {
                  log.info("正常");
                } else {
                  log.error("出现异常", ar.cause());
                }
                handler.handle(simpleExecutionContext);
              });
    }
  }

  private void send(
      SimpleExecutionContext simpleExecutionContext,
      ReactorHandler chainHandler,
      HttpRequest<Buffer> httpRequest) {

    Api api = (Api) chainHandler.getReactable();

    // 获得后台服务方法
    HttpMethod httpMethod = HttpMethod.valueOf(api.getServiceHttpMethod().toUpperCase(Locale.ROOT));

    /** 未处理的请求体 */
    Buffer proxyRequestBody = simpleExecutionContext.getProxyRequestBody();
    /** 处理后的请求体 */
    String proxyRequestBodyAfterRewrite = simpleExecutionContext.getProxyRequestBodyAfterRewrite();

    // 没有熔断器
    Future<HttpResponse<Buffer>> future;

    if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
        && simpleExecutionContext
            .getHeaders()
            .get("Content-Type")
            .toLowerCase(Locale.ROOT)
            .contains("multipart/form-data")) {

      MultipartForm multipartForm = getFormDataParts(simpleExecutionContext);

      future = httpRequest.sendMultipartForm(multipartForm);
    } else if (StringUtils.equals(HttpMethod.POST.name(), httpMethod.name())
        && simpleExecutionContext
            .getHeaders()
            .get("Content-Type")
            .toLowerCase(Locale.ROOT)
            .contains("application/x-www-form-urlencoded")) {

      future = httpRequest.sendForm(simpleExecutionContext.getProxyFormAttributes());
    } else {
      if (StringUtils.isNotEmpty(proxyRequestBodyAfterRewrite)) {
        future = httpRequest.sendBuffer(Buffer.buffer(proxyRequestBodyAfterRewrite));
      } else if (proxyRequestBody != null && proxyRequestBody.length() > 0) {
        future = httpRequest.sendBuffer(proxyRequestBody);
      } else {
        future = httpRequest.send();
      }
    }

    future
        .onSuccess(
            response -> {
              // 设置服务端响应
              simpleExecutionContext.setProxyResult(response.bodyAsBuffer());
              // 设置网关响应结果
              simpleExecutionContext.setRespResult(response.bodyAsBuffer());

              simpleExecutionContext.setProxyRespHeaders(response.headers());

              simpleExecutionContext.setRespHeaders(response.headers());

              simpleExecutionContext.setProxyCookies(response.cookies());
              chainHandler.handle(simpleExecutionContext);
            })
        .onFailure(
            ex -> {
              log.error("代理服务出现错误", ex);
              // 设置服务端响应
              simpleExecutionContext.setProxyResult(Buffer.buffer(ex.toString()));
              // 设置网关响应结果
              simpleExecutionContext.setRespResult(Buffer.buffer(ex.toString()));

              /** @updator JiangLei 继续向下执行，并将异常记录 */
              throwPluginException(simpleExecutionContext, ex);

              chainHandler.handle(simpleExecutionContext);
            });
  }
  /**
   * <构造表单参数>
   *
   * @param simpleExecutionContext 上下文
   * @return io.vertx.ext.web.multipart.MultipartForm
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/27 10:52
   */
  private MultipartForm getFormDataParts(SimpleExecutionContext simpleExecutionContext) {

    MultipartForm multipartForm = MultipartForm.create();
    if (simpleExecutionContext.getProxyFormAttributes() != null
        && !simpleExecutionContext.getProxyFormAttributes().isEmpty()) {
      simpleExecutionContext
          .getProxyFormAttributes()
          .forEach(
              multiMap -> {
                multipartForm.attribute(multiMap.getKey(), multiMap.getValue());
              });
    }
    if (CollectionUtils.isNotEmpty(simpleExecutionContext.getProxyUploadFiles())) {
      simpleExecutionContext
          .getProxyUploadFiles()
          .forEach(
              fileUpload -> {
                multipartForm.textFileUpload(
                    fileUpload.name(),
                    fileUpload.fileName(),
                    fileUpload.uploadedFileName(),
                    fileUpload.contentType());
              });
    }
    return multipartForm;
  }

  private void handleRespAsync(
      SimpleExecutionContext simpleExecutionContext,
      ReactorHandler handler,
      io.vertx.core.Promise<Void> promise,
      io.vertx.core.AsyncResult<HttpResponse<Buffer>> httpResponseAsyncResult) {
    final HttpResponse<Buffer> response = httpResponseAsyncResult.result();
    if (response == null || response.statusCode() != 200) {

      Throwable ex = httpResponseAsyncResult.cause();
      promise.fail(ex);
      log.error("代理服务出现错误", ex);
      // 设置服务端响应
      simpleExecutionContext.setProxyResult(Buffer.buffer(ex.toString()));
      if (response != null) {
        // 设置服务端响应码
        simpleExecutionContext.setProxyStatusCode(response.statusCode());
      }
      // 设置网关响应结果
      simpleExecutionContext.setRespResult(Buffer.buffer(ex.toString()));
      /** @updator JiangLei 继续向下执行，并将异常记录 */
      super.throwPluginException(simpleExecutionContext, ex);
    } else {

      promise.complete();
      // 设置服务端响应
      simpleExecutionContext.setProxyResult(response.bodyAsBuffer());
      // 设置网关响应结果
      simpleExecutionContext.setRespResult(response.bodyAsBuffer());

      simpleExecutionContext.setProxyRespHeaders(response.headers());

      simpleExecutionContext.setRespHeaders(response.headers());

      simpleExecutionContext.setProxyStatusCode(response.statusCode());

      simpleExecutionContext.setProxyCookies(response.cookies());
    }
  }

  private Function<? super Vertx, ? extends WebClient> createWebClient(Vertx vertx) {

    WebClientOptions webClientOptions = new WebClientOptions();

    webClientOptions.setConnectTimeout(3000);

    webClientOptions.setIdleTimeout(5000);

    return context -> WebClient.create(vertx, webClientOptions);
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
    return Boolean.FALSE;
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
    return "httpProxy";
  }
}
