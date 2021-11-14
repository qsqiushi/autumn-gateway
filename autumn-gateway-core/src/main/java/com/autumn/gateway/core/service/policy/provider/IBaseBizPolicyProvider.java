package com.autumn.gateway.core.service.policy.provider;

import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;

import java.util.List;

/**
 * <基础策略提供插件>
 *
 * @author qiushi
 * @since 2021/8/14 10:10
 */
public interface IBaseBizPolicyProvider extends IPolicyProvider {

  /**
   * <获取插件信息>
   *
   * @param
   * @return : io.vertx.core.json.JsonArray
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/13 16:47
   */
  List<PluginConfigInfo> getPluginsInfo(String... bizId);
}
