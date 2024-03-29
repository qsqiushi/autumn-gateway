package com.autumn.gateway.component.policy.provider.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import com.autumn.gateway.component.policy.provider.common.SysPolicyConstants;
import com.autumn.gateway.component.policy.provider.enums.RedisKeyEnums;
import com.autumn.gateway.component.policy.provider.util.PolicyUtil;
import com.autumn.gateway.core.service.policy.provider.IProductPolicyProvider;
import com.autumn.gateway.data.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiushi
 * @program  autumn
 * @description app策略
 * @since 2021-08-12:15:49
 */
@Slf4j
@Service
public class ProductPolicyProviderImpl implements IProductPolicyProvider {

  @Autowired
  private RedisService<String> redisService;

  /**
   * <获取插件信息>
   *
   * @return : io.vertx.core.json.JsonArray
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/13 16:47
   */
  @Override
  public List<PluginConfigInfo> getPluginsInfo(String... bizId) {

    String key = String.format(RedisKeyEnums.AUTUMN_PRODUCT_POLICY.getKeyPattern(), bizId);

    String plugins = redisService.hget(key, SysPolicyConstants.PLUGIN);

    return PolicyUtil.explainPluginConfig(plugins);
  }
}
