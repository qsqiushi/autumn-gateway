package com.autumn.gateway.component.policy.provider.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import com.autumn.gateway.component.policy.provider.common.SysPolicyConstants;
import com.autumn.gateway.component.policy.provider.enums.RedisKeyEnums;
import com.autumn.gateway.component.policy.provider.util.PolicyUtil;
import com.autumn.gateway.core.service.policy.provider.ISysPolicyProvider;
import com.autumn.gateway.data.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qiushi
 * @program agw
 * @description 系统策略
 * @since create 2021-08-12:15:49
 */
@Slf4j
@Service
public class SysPolicyProviderImpl implements ISysPolicyProvider {

  @Resource private RedisService<String> redisService;

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

    String plugins =
        redisService.hget(RedisKeyEnums.AGW_SYS_POLICY.getKeyPattern(), SysPolicyConstants.PLUGIN);

    return PolicyUtil.explainPluginConfig(plugins);
  }

  /**
   * <获取域名>
   *
   * @return java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 19:36
   */
  @Override
  public String getDomain() {

    return redisService.hget(
        RedisKeyEnums.AGW_SYS_POLICY.getKeyPattern(), SysPolicyConstants.DOMAIN);
  }

  /**
   * <获取所有系统策略>
   *
   * @return java.util.Map<java.lang.String, java.lang.String>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/13 08:27
   */
  @Override
  public Map<String, String> getSysPolicy() {
    return redisService.hmget(RedisKeyEnums.AGW_SYS_POLICY.getKeyPattern());
  }
}
