package com.autumn.gateway.api.plugin.core.api.context;

import com.autumn.gateway.api.plugin.core.api.pojo.AgwFileUpload;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.common.constant.ReqConstants;
import com.autumn.gateway.common.util.UrlMatcherUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.http.impl.headers.HeadersMultiMap;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program autumn-gateway
 * @description 可执行上下文
 * @author qiushi
 * @since 2021-07-07:14:27
 */
@Slf4j
@Data
@Accessors(chain = true)
public class SimpleExecutionContext {

  public static final String ATTR_PREFIX = "qm.gateway.attribute.";

  /** 访问网关的路径 */
  public static final String ATTR_HTTP_URL = "httpUrl";

  public static final String ATTR_INVOKER = "request.invoker";

  public static final String ATTR_API_ID = "api.id";

  public static final String ATTR_TAKE_TIMES = "takeTimes.";

  public static final String ATTR_APP_KEY = "appKey.";

  public static final String START_TIME = "startTime";

  public static final String END_TIME = "endTime";

  public static final String CLASS_NAME = "className";

  public static final String ATTR_PROXY = "proxy.";

  public static final String ATTR_PARAM = "param.";

  public static final String ATTR_AUTH = "auth.";

  public static final String ATTR_RESP = "resp.";

  /** 所有插件的异常列表，供日志或其他插件使用 */
  public static final String ATTR_PLUGIN_EXCEPTION_LIST_KEY = "plugin.exception.list.key";

  /** 代理插件参数前缀 */
  public static final String PROXY_PLUGIN_PREFIX =
          SimpleExecutionContext.ATTR_PREFIX + SimpleExecutionContext.ATTR_PROXY;

  /** 代理插件开始时间 */
  public static final String PROXY_START_TIME_KEY = PROXY_PLUGIN_PREFIX + START_TIME;

  /** 代理插件结束时间 */
  public static final String PROXY_END_TIME_KEY = PROXY_PLUGIN_PREFIX + END_TIME;

  /** 后台访问路径 */
  public static final String PROXY_REAL_URL_KEY = PROXY_PLUGIN_PREFIX + "realUrl";
  /** 代理插件类 */
  public static final String PROXY_CLASS_KEY = PROXY_PLUGIN_PREFIX + CLASS_NAME;

  /** 参数解析插件 */
  public static final String PARAM_PLUGIN_PREFIX = ATTR_PREFIX + ATTR_PARAM;

  /** 参数解析插件开始时间 */
  public static final String PARAM_START_TIME_KEY = PARAM_PLUGIN_PREFIX + START_TIME;

  /** 参数解析插件结束时间 */
  public static final String PARAM_END_TIME_KEY = PARAM_PLUGIN_PREFIX + END_TIME;

  /** 参数解析插件类 */
  public static final String PARAM_CLASS_KEY = PARAM_PLUGIN_PREFIX + CLASS_NAME;

  /** 鉴权解析插件 */
  public static final String AUTH_PLUGIN_PREFIX = ATTR_PREFIX + ATTR_AUTH;

  /** 鉴权插件开始时间 */
  public static final String AUTH_START_TIME_KEY = AUTH_PLUGIN_PREFIX + START_TIME;

  /** 鉴权插件结束时间 */
  public static final String AUTH_END_TIME_KEY = AUTH_PLUGIN_PREFIX + END_TIME;

  /** 鉴权插件类 */
  public static final String AUTH_CLASS_KEY = AUTH_PLUGIN_PREFIX + CLASS_NAME;

  /** 鉴权插件签名类 */
  public static final String AUTH_SING_CLASS_KEY = AUTH_PLUGIN_PREFIX + "sign." + CLASS_NAME;

  /** 鉴权插件子链开始时间 */
  public static final String AUTH_EXPAND_START_TIME_KEY =
          AUTH_PLUGIN_PREFIX + "expand." + START_TIME;

  /** 鉴权插件子链结束时间 */
  public static final String AUTH_EXPAND_END_TIME_KEY = AUTH_PLUGIN_PREFIX + "expand." + END_TIME;

  /** 返回值重写插件 */
  public static final String RESP_PLUGIN_PREFIX = ATTR_PREFIX + ATTR_RESP;

  /** 返回值重写开始时间 */
  public static final String RESP_START_TIME_KEY = RESP_PLUGIN_PREFIX + START_TIME;

  /** 返回值重写结束时间 */
  public static final String RESP_END_TIME_KEY = RESP_PLUGIN_PREFIX + END_TIME;

  /** 返回值重写插件类 */
  public static final String RESP_CLASS_KEY = RESP_PLUGIN_PREFIX + CLASS_NAME;

  /** 开始处理请求的时间 */
  private Long startTime;

  /** 请求返回的时间 */
  private Long endTime;

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
  private String requestBody;

  /** 上传文件 */
  private List<FileUpload> uploadFiles;

  /** 表单数据 */
  private MultiMap formAttributes;

  /** 网关的响应码 */
  private Integer statusCode;

  // 访问网关的请求------------------------------end

  // 网关访问服务端-------------------------------start

  /** 路径参数 /api/{test}/get */
  private Map<String, String> proxyPathParameters = null;

  /** get请求?后面的参数 */
  private MultiMap proxyQueryParameters = null;

  /** 上传文件 */
  private List<AgwFileUpload> proxyUploadFiles;

  /** 表单数据 */
  private MultiMap proxyFormAttributes;

  /** 向服务端推送的请求体 */
  private String proxyRequestBody;

  /** 向服务端推送的 请求头 */
  private MultiMap proxyHeaders = null;

  /** 服务端的响应结果 */
  private String proxyResult;

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
  /** 网关的响应结果 */
  private String respResult;
  // 网关响应 -----------------------------end

  /** 状态 真个链路是否有错误 */
  private Boolean hasError = false;

  /**
   * <上下文中是否遇到错误>
   *
   * @param
   * @return : java.lang.Boolean
   * @author qiushi
   * @updater qiushi
   * @since 2021/9/7 09:23
   */
  public Boolean getHasError() {
    return hasError;
  }
  /**
   * <上下文中遇到错误>
   *
   * @param
   * @author qiushi
   * @updater qiushi
   * @since 2021/9/7 09:24
   */
  public void errorEncountered() {
    this.hasError = Boolean.TRUE;
  }

  public SimpleExecutionContext(RoutingContext routingContext, Api api) {



    if (StringUtils.isNumeric(ThreadContext.get(ReqConstants.START_TIME))) {
      this.startTime = Long.parseLong(ThreadContext.get(ReqConstants.START_TIME));
    } else {
      this.startTime = System.currentTimeMillis();
    }

    this.routingContext = routingContext;
    // API id
    this.getAttributes().put(SimpleExecutionContext.ATTR_API_ID, api.getApiId());

    this.requestBody = routingContext.body().asString();

    this.queryParameters = routingContext.queryParams();

    this.headers = routingContext.request().headers();

    /** 是否移动到代理请求头 去掉无效的请求头 */
    this.headers
            .remove("Accept")
            .remove("Accept-Encoding")
            .remove("Connection")
            .remove("Host")
            .remove("Upgrade-Insecure-Requests")
            .remove("User-Agent")
            .remove("Accept-Language")
            .remove("Accept-Encoding")
            .remove("Content-Length");

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
