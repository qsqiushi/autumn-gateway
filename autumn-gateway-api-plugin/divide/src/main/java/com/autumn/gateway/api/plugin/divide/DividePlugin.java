package com.autumn.gateway.api.plugin.divide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.autumn.gateway.api.plugin.core.AbstractPlugin;
import com.autumn.gateway.api.plugin.core.api.context.SimpleExecutionContext;
import com.autumn.gateway.api.plugin.core.api.handler.ReactorHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.enums.ApiPluginTypeEnum;
import com.autumn.gateway.api.plugin.core.enums.RoutingStgyEnum;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.api.plugin.core.pojo.RouteParam;
import com.autumn.gateway.api.plugin.divide.cache.ServerCache;
import com.autumn.gateway.api.plugin.divide.constants.RuleConstants;
import com.autumn.gateway.api.plugin.divide.pojo.UserDefinedProxyEndpoint;
import com.autumn.gateway.api.plugin.divide.pojo.UserDefinedServer;
import com.autumn.gateway.common.constant.SpecialSymbolsConstants;
import com.autumn.gateway.common.exception.BizRunTimeException;
import com.autumn.gateway.common.util.CommonUtil;
import com.autumn.gateway.common.util.UrlMatcherUtil;
import com.google.api.client.http.UriTemplate;
import com.jayway.jsonpath.JsonPath;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.IRule;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiushi
 * @program agw
 * @description divide插件
 * @since 2021-08-20:14:02
 */
@Slf4j
public class DividePlugin extends AbstractPlugin {

  public DividePlugin(PluginParam pluginParam) {
    super(pluginParam);
  }

  /**
   * <执行插件>
   *
   * @param ctx 上下文
   * @author qiushi
   * @updater qiushi
   * @since 2021/7/12 14:05
   */
  @Override
  protected void doExecute(SimpleExecutionContext ctx) {
    // DO NOTHING
  }

  protected void doExecute(SimpleExecutionContext ctx, Api api) {
    // api路由策略
    Integer stgy = api.getRoutingStgy();
    RoutingStgyEnum routingStgyEnum = RoutingStgyEnum.getByCode(stgy);
    if (routingStgyEnum == null) {
      ctx.errorEncountered();
      log.error("路由策略不匹配");
      return;
    }
    // 负载均衡规则
    IRule rule;
    switch (routingStgyEnum) {
      case SINGLE_RULE:
        singleNode(ctx, api);
        break;
      case AVAILABILITY_FILTERING_RULE:
        rule = RuleConstants.AVAILABILITY_FILTERING_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case RANDOM_RULE:
        rule = RuleConstants.RANDOM_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case RETRY_RULE:
        rule = RuleConstants.RETRY_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case WEIGHTED_RESPONSE_TIME_RULE:
        rule = RuleConstants.WEIGHTED_RESPONSE_TIME_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case ROUND_ROBIN_RULE:
        rule = RuleConstants.ROUND_ROBIN_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case BEST_AVAILABLE_RULE:
        rule = RuleConstants.BEST_AVAILABLE_RULE;
        multiNodeRule(ctx, api, rule);
        break;
      case CONDITION_RULE:
        multiNodeCondition(ctx, api);
        break;
      default:
        throw new BizRunTimeException("非法路由策略");
    }
  }
  /**
   * <多节点条件路由>
   *
   * @param ctx 上下文
   * @param api api
   * @author qiushi
   * @updater qiushi
   * @since 2022/2/17 19:14
   */
  private void multiNodeCondition(SimpleExecutionContext ctx, Api api) {
    String url;
    JsonArray array = new JsonArray(getPluginParam().getJsonParam());
    // 解析配置获得服务节点
    List<UserDefinedProxyEndpoint> endpoints =
        array.stream()
            .map(
                item -> {
                  JsonObject jsonObject = (JsonObject) item;
                  return jsonObject.mapTo(UserDefinedProxyEndpoint.class);
                })
            .collect(Collectors.toList());

    UserDefinedProxyEndpoint accordEndPoint = getAccordEndPoint(ctx, endpoints);

    url = accordEndPoint.getTarget();
    // 获得后台服务路径 谷歌已经处理是否包含路径参数 参数是否空
    url = UriTemplate.expand(url, ctx.getProxyPathParameters(), false);

    if (url.endsWith(SpecialSymbolsConstants.SLASH)
        || url.length() - url.replaceAll(SpecialSymbolsConstants.DOUBLE_SLASH, "").length()
            > SpecialSymbolsConstants.DOUBLE_SLASH.length()) {
      ctx.errorEncountered();
      log.error("参数数量不匹配");
    }

    if (log.isDebugEnabled()) {
      log.debug("Service request path is [{}]", url);
    }
    // 判断url是否需要拼接
    if (Boolean.TRUE.equals(accordEndPoint.getPathJoin())) {
      // **的部分
      String patternMapped =
          UrlMatcherUtil.extractPathWithinPattern(
              api.getUrl(), ctx.getRoutingContext().request().path());
      // ** 必须在路径的最后
      url = url.replace(SpecialSymbolsConstants.DOUBLE_ASTERISK, patternMapped);
    }

    ctx.getAttributes().put(SimpleExecutionContext.ATTR_HTTP_URL, url);
  }

  /**
   * <单节点处理>
   *
   * @param ctx 上下文
   * @param api api
   * @author qiushi
   * @updater qiushi
   * @since 2022/2/17 19:07
   */
  private void singleNode(SimpleExecutionContext ctx, Api api) {

    String url;
    url = api.getConnectUrl();
    url = UriTemplate.expand(url, ctx.getProxyPathParameters(), false);
    if (log.isDebugEnabled())
    {
      log.debug("api[{}]采用单节点策略,实际请求地址为[{}]", api.getName(), url);
    }
    // 如果需要拼接 并且包含**
    if (Boolean.TRUE.equals(api.getPathJoin())
        && url.contains(SpecialSymbolsConstants.DOUBLE_ASTERISK)) {
      // **的部分
      String patternMapped =
          UrlMatcherUtil.extractPathWithinPattern(
              api.getUrl(), ctx.getRoutingContext().request().path());
      // ** 必须在路径的最后
      url = url.replace(SpecialSymbolsConstants.DOUBLE_ASTERISK, patternMapped);
    }
    ctx.getAttributes().put(SimpleExecutionContext.ATTR_HTTP_URL, url);
  }
  /**
   * <多服务系统规则>
   *
   * @param ctx 上下文
   * @param api api
   * @param rule 规则
   * @author qiushi
   * @updater qiushi
   * @since 2022/2/17 19:10
   */
  private void multiNodeRule(SimpleExecutionContext ctx, Api api, IRule rule) {
    // 构造负载均衡器
    DynamicServerListLoadBalancer<UserDefinedServer> dynamicServerListLoadBalancer =
        ServerCache.API_LOAD_BALANCER.computeIfAbsent(
            api.getApiId(),
            k -> {
              // 获取负载均衡插件配置
              JsonArray array = new JsonArray(getPluginParam().getJsonParam());
              // 解析配置获得服务节点
              List<UserDefinedServer> endpoints =
                  array.stream()
                      .map(
                          item -> {
                            JsonObject jsonObject = (JsonObject) item;
                            UserDefinedProxyEndpoint endpoint =
                                jsonObject.mapTo(UserDefinedProxyEndpoint.class);
                            return new UserDefinedServer(endpoint);
                          })
                      .collect(Collectors.toList());
              DynamicServerListLoadBalancer<UserDefinedServer> current =
                  new DynamicServerListLoadBalancer<>();

              current.setRule(rule);
              endpoints.forEach(current::addServer);

              return current;
            });
    // 选择server
    UserDefinedServer server = (UserDefinedServer) dynamicServerListLoadBalancer.chooseServer();

    String url = server.getUrl();
    // 获得后台服务路径 谷歌已经处理是否包含路径参数 参数是否空
    url = UriTemplate.expand(url, ctx.getProxyPathParameters(), false);

    if (url.endsWith(SpecialSymbolsConstants.SLASH)
        || url.length() - url.replaceAll(SpecialSymbolsConstants.DOUBLE_SLASH, "").length()
            > SpecialSymbolsConstants.DOUBLE_SLASH.length()) {
      ctx.errorEncountered();
      log.error("参数数量不匹配");
    }

    if (log.isDebugEnabled()) {
      log.debug("Service request path is [{}]", url);
    }

    // 判断url是否需要拼接
    if (Boolean.TRUE.equals(server.getUserDefinedProxyEndpoint().getPathJoin())) {
      // **的部分
      String patternMapped =
          UrlMatcherUtil.extractPathWithinPattern(
              api.getUrl(), ctx.getRoutingContext().request().path());
      // ** 必须在路径的最后
      url = url.replace(SpecialSymbolsConstants.DOUBLE_ASTERISK, patternMapped);
    }

    ctx.getAttributes().put(SimpleExecutionContext.ATTR_HTTP_URL, url);
  }

  @Override
  public void execute(
      SimpleExecutionContext ctx, ReactorHandler<SimpleExecutionContext, Api> handler) {
    Boolean doExecute = CommonUtil.getBoolean();
    if (Boolean.TRUE.equals(ctx.getHasError())
        && Boolean.FALSE.equals(errorEncounteredContinue())) {
      doExecute = false;
    }

    if (Boolean.TRUE.equals(doExecute)) {
      try {

        Api api = handler.getReactable();
        if (log.isDebugEnabled()) {
          log.debug(
              "api[{}]的[{}]插件的策略[{}]",
              api.getUrl(),
              this.getPluginId(),
              this.getPluginParam().getJsonParam());
        }

        doExecute(ctx, handler.getReactable());
      } catch (Exception e) {
        throwPluginException(ctx, e);
        log.error("异常",e);
      }
    }
    handler.handle(ctx);
  }

  /**
   * <获得插件类型>
   *
   * @return :
   * @author qiushi
   * @updater qiushi
   * @since 2021/6/24 19:15
   */
  @Override
  public ApiPluginTypeEnum getPluginType() {
    return ApiPluginTypeEnum.REQUEST;
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
    return "divide";
  }
  /**
   * <获取匹配的节点>
   *
   * @param ctx 上下文
   * @param endpoints 节点
   * @return com.qm.agw.pluggable.divide.pojo.UserDefinedProxyEndpoint
   * @author qiushi
   * @updater qiushi
   * @since 2022/2/17 19:18
   */
  private UserDefinedProxyEndpoint getAccordEndPoint(
      SimpleExecutionContext ctx, List<UserDefinedProxyEndpoint> endpoints) {
    // 寻找匹配的路由
    for (UserDefinedProxyEndpoint endpoint : endpoints) {
      // 匹配的个数
      List<Boolean> accords = new ArrayList<>();
      // 不匹配的个数
      List<Boolean> unAccords = new ArrayList<>();
      endPointMatchingDegree(ctx, endpoint, accords, unAccords);
      // 且条件
      if (endpoint.getEffMode() == 1 && !accords.isEmpty() && unAccords.isEmpty()) {
        return endpoint;
      }
      // 条件
      if (endpoint.getEffMode() == 2 && !accords.isEmpty()) {
        return endpoint;
      }
    }
    throw new BizRunTimeException("没有匹配的节点");
  }
  /**
   * <计算节点的匹配程度>
   *
   * @param ctx 上下文
   * @param endpoint 节点
   * @param accords 正确的匹配条件个数
   * @param unAccords 错误的匹配条件个数
   * @author qiushi
   * @updater qiushi
   * @since 2022/2/17 19:29
   */
  private void endPointMatchingDegree(
      SimpleExecutionContext ctx,
      UserDefinedProxyEndpoint endpoint,
      List<Boolean> accords,
      List<Boolean> unAccords) {

    for (RouteParam routeParam : endpoint.getRouteParamList()) {
      String value = null;
      switch (routeParam.getParamLocation()) {
        case "HeaderParam":
          value = ctx.getHeaders().get(routeParam.getParamName());
          break;
        case "QueryParam":
          value = ctx.getQueryParameters().get(routeParam.getParamName());
          break;
        case "FormParam":
          value = ctx.getFormAttributes().get(routeParam.getParamName());
          break;
        case "BodyParam":
          String body = ctx.getRequestBody().toString();
          // routeParam.getParamLocation() example "$.method"
          // @See https://github.com/json-path/JsonPath
          // 如果没有值  read方法就会报错
          value = JsonPath.read(body, routeParam.getParamLocation()).toString();
          break;
        case "PathParam":
          value = ctx.getPathParameters().get(routeParam.getParamLocation());
          break;
        default:
          break;
      }
      if (value == null) {
        continue;
      }
      int compare = value.compareTo(routeParam.getParamValue());
      if (compare == 0 && routeParam.getParamCondition() == 0) {
        accords.add(true);
        return;
      }
      if (compare > 0 && routeParam.getParamCondition() > 0) {
        accords.add(true);
        return;
      }
      if (compare < 0 && routeParam.getParamCondition() < 0) {
        accords.add(true);
        return;
      }
      unAccords.add(false);
    }
  }
}
