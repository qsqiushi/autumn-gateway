package com.autumn.gateway.api.plugin.core.api.context;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.common.util.UrlMatcherUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.impl.headers.HeadersMultiMap;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program  autumn-gateway
 * @description 可执行上下文
 * @author qiushi
 * @since 2021-07-07:14:27
 */
@Data
@Accessors(chain = true)
public class SimpleExecutionContext {

  public static final String ATTR_PREFIX = "gateway.attribute.";

  public static final String ATTR_HTTP_URL = "httpUrl";

  public static final String ATTR_INVOKER = "request.invoker";

  public static final String ATTR_API_ID = "api.id";

  public static final String ATTR_TAKE_TIMES = "takeTimes.";

  public static final String ATTR_APP_KEY = "appKey";

  /** 所有插件的异常列表，供日志或其他插件使用 */
  public static final String ATTR_PLUGIN_EXCEPTION_LIST_KEY = "plugin_exception_list_key";

  /** 网关参数 */
  private final Map<String, Object> attributes = new AttributeMap();

  // 访问网关的请求------------------------------start

  /** 访问网关的请求 */
  private RoutingContext routingContext;

  /** get请求?后面的参数 */
  private MultiMap queryParameters = new HeadersMultiMap();

  /** 路径参数 /api/{test}/get */
  private Map<String, String> pathParameters = new HashMap<>();

  /** 请求头 */
  private MultiMap headers = new HeadersMultiMap();

  /** 请求体 */
  private Buffer requestBody;

  /** 上传文件 */
  private Set<FileUpload> uploadFiles;

  /** 表单数据 */
  private MultiMap formAttributes;

  // 访问网关的请求------------------------------end

  // 网关访问服务端-------------------------------start

  /** 路径参数 /api/{test}/get */
  private Map<String, String> proxyPathParameters = null;

  /** get请求?后面的参数 */
  private MultiMap proxyQueryParameters = null;

  /** 上传文件 */
  private Set<FileUpload> proxyUploadFiles;

  /** 表单数据 */
  private MultiMap proxyFormAttributes;

  /** 向服务端推送的请求体 */
  private Buffer proxyRequestBody;

  /** 向服务端推送的 修改之后的 请求体 */
  private String proxyRequestBodyAfterRewrite;

  /** 向服务端推送的 请求头 */
  private MultiMap proxyHeaders = null;

  /** 服务端的响应结果 */
  private Buffer proxyResult;

  /** 服务端的响应结果cookies */
  private List<String> proxyCookies;

  /** 服务端返回的请求头 */
  private MultiMap proxyRespHeaders;

  /** 服务端的响应码 */
  private Integer proxyStatusCode;

  // 网关访问服务端-------------------------------end

  // 网关响应 -----------------------------start

  /** 网关的响应结果 */
  private MultiMap respHeaders = new HeadersMultiMap();

  private Buffer respResult;
  /** 处理后网关的响应结果 */
  private String respResultAfterRewrite;

  // 网关响应 -----------------------------end

  /** 状态 真个链路是否有错误 */
  private Boolean hasError = false;

  /**
   * <上下文中是否遇到错误>
   *
   * @param
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:23
   */
  public Boolean getHasError() {
    return hasError;
  }
  /**
   * <上下文中遇到错误>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 09:24
   */
  public void errorEncountered() {
    this.hasError = Boolean.TRUE;
  }

  public SimpleExecutionContext(RoutingContext routingContext, Api api) {
    this.routingContext = routingContext;
    // API id
    this.getAttributes().put(SimpleExecutionContext.ATTR_API_ID, api.getApiId());

    this.requestBody = routingContext.getBody();

    this.queryParameters = routingContext.queryParams();

    this.headers = routingContext.request().headers();

    this.pathParameters =
        UrlMatcherUtil.extractUriTemplateVariables(api.getUrl(), routingContext.request().path());

    this.uploadFiles = routingContext.fileUploads();

    this.formAttributes = routingContext.request().formAttributes();
  }

  private class AttributeMap extends HashMap<String, Object> {

    /**
     * In the most general case, the context will not store more than 12 elements in the Map. Then,
     * the initial capacity must be set to limit size in memory.
     */
    AttributeMap() {
      super(12, 1.0f);
    }

    @Override
    public Object get(Object key) {
      Object value = super.get(key);
      return (value != null) ? value : super.get(SimpleExecutionContext.ATTR_PREFIX + key);
    }
  }
}
