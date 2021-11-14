package com.autumn.gateway.starter.vertx.verticle;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import com.autumn.gateway.service.impl.Pf4jPluginManagerServiceImpl;
import com.autumn.gateway.util.ApplicationContextUtil;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @program autumn-gateway
 * @description 部署api同步 在application.json 中使用
 * @author qiushi
 * @since 2021-06-24:09:54
 */
@Slf4j
public class ApiSyncVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    // 获取 API发现实例 API注册实例
    /** @see Pf4jPluginManagerServiceImpl#loadComponent() */
    IApiDiscovererService apiDiscovererService =
        ApplicationContextUtil.getBean(IApiDiscovererService.class);
    IApiRegisterService apiRegisterService =
        ApplicationContextUtil.getBean(IApiRegisterService.class);

    long start = System.currentTimeMillis();
    super.start();
    // 获取所有API
    Collection<Api> apiList = apiDiscovererService.apis();
    apiRegisterService.setApis(new ArrayList<>(apiList));
    // 注册API
    for (Api api : apiList) {
      apiRegisterService.register(api);
    }
    log.info(
        "ApiSyncVerticle work has been finished,it took [{}] ms",
        System.currentTimeMillis() - start);
  }
}
