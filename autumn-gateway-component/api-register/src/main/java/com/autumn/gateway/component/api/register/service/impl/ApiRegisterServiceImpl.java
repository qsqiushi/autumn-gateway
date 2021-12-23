package com.autumn.gateway.component.api.register.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.common.util.UrlMatcherUtil;
import com.autumn.gateway.core.event.EventManager;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program agw
 * @description
 * @author qiushi
 * @since 2021-07-26:14:27
 */
@Slf4j
@Service
public class ApiRegisterServiceImpl implements IApiRegisterService {

  private Map<String, Api> apis;

  @Resource private IApiDiscovererService apiDiscovererService;

  @Resource private EventManager eventManager;

  /**
   * Register an API definition. It is a "create or update" operation, if the api was previously
   * existing, the definition is updated accordingly.
   *
   * @param api
   * @return
   */
  @Override
  public boolean register(Api api) {

    Api deployedApi = get(api.getApiId());
    // TODO 更新API状态 判断API状态

    // 发布消息 交给监听处理
    /**
     * @see com.qm.agw.core.event.impl.EventManagerImpl
     * @see ApiReactorEventListener 在start
     */
    eventManager.publishEvent(ApiReactorEvent.DEPLOY, deployedApi);

    return true;
  }

  /**
   * <>
   *
   * @return boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/12/23 09:09
   */
  @Override
  public boolean registerAll() {
    initApiMap();
    for (Api api : apis.values()) {
      register(api);
    }
    return true;
  }

  /**
   * <取消注册>
   *
   * @param apiId
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 15:06
   */
  @Override
  public void unregister(String apiId) {

    Api api = get(apiId);
    // TODO 更新API状态
    eventManager.publishEvent(ApiReactorEvent.UNDEPLOY, api);
  }

  /**
   * <>
   *
   * @param api
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 15:08
   */
  @Override
  public void reRegister(Api api) {
    // TODO 更新API状态
    Api reRegisterApi = get(api.getApiId());
    eventManager.publishEvent(ApiReactorEvent.UPDATE, reRegisterApi);
  }

  /**
   * <获得匹配的API>
   *
   * @param httpServerRequest
   * @return : Api
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/22 13:37
   */
  @Override
  public Api getMatch(HttpServerRequest httpServerRequest) {
    initApiMap();
    Collection<Api> apis = this.apis.values();
    List<Api> matchApiList =
        apis.stream()
            .filter(
                temp -> {
                  // 路径匹配
                  boolean pathMatch = accept(temp, httpServerRequest);
                  if (pathMatch) {
                    // 方法匹配
                    return StringUtils.equals(
                        temp.getMethod().toUpperCase(Locale.ROOT),
                        httpServerRequest.method().toString());
                  } else {
                    return false;
                  }
                })
            .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(matchApiList) || matchApiList.size() != 1) {
      // TODO 若仍有多个 可以比对参数等
      log.error("找到匹配的API[{}]", matchApiList.size());
      return null;
    }
    return matchApiList.get(0);
  }

  @Override
  public void initApiMap() {
    if (CollectionUtils.isEmpty(apis)) {
      this.apis =
          apiDiscovererService.apis().stream().collect(Collectors.toMap(Api::getApiId, e -> e));
    }
  }

  private Api get(String apiId) {
    initApiMap();
    return apis.get(apiId);
  }

  private boolean accept(Api api, HttpServerRequest request) {
    return UrlMatcherUtil.match(api.getUrl(), request.path());
  }
}
