package com.autumn.gateway.component.policy.provider.service.impl;

import com.autumn.gateway.component.policy.provider.common.PluginPolicyConstants;
import com.autumn.gateway.component.policy.provider.enums.RedisKeyEnums;
import com.autumn.gateway.core.service.policy.provider.IPluginPolicyProvider;
import com.autumn.gateway.data.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qiushi
 * @program  autumn
 * @description 插件策略提供
 * @since 2021-08-12:15:59
 */
@Slf4j
@Service
public class PluginPolicyProviderImpl implements IPluginPolicyProvider {

  @Autowired
  private RedisService<String> redisService;

  /**
   * <根据ID查询策略>
   *
   * @param policyId
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/14 14:57
   */
  @Override
  public String getPolicy(String policyId) {
    String key = String.format(RedisKeyEnums.AUTUMN_PLUGIN_POLICY.getKeyPattern(), policyId);
    return redisService.hget(key, PluginPolicyConstants.CONTENT);
  }
}
