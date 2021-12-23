package com.autumn.gateway.core.service.register;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.service.IService;
import io.vertx.core.http.HttpServerRequest;

/**
 * @program qm-gateway
 * @description
 * @author qiushi
 * @since 2021-06-18:15:06
 */
public interface IApiRegisterService extends IService {

  /**
   * Register an API definition. It is a "create or update" operation, if the api was previously
   * existing, the definition is updated accordingly.
   *
   * @param api
   * @return
   */
  boolean register(Api api);
  /**
   * <>
   *
   * @param
   * @return boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/12/23 09:09
   */
  boolean registerAll();
  /**
   * <取消注册>
   *
   * @param apiId
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 15:06
   */
  void unregister(String apiId);
  /**
   * <>
   *
   * @param api
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/6/18 15:08
   */
  void reRegister(Api api);
  /**
   * <获得匹配的API>
   *
   * @param httpServerRequest
   * @return : Api
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/22 13:37
   */
  Api getMatch(HttpServerRequest httpServerRequest);
  /**
   * <初始化API map>
   *
   * @author qiushi
   * @updator qiushi
   * @since 2021/12/14 19:18
   */
  void initApiMap();
}
