package com.autumn.gateway.starter.biz.service.impl;

import com.autumn.gateway.core.service.plugin.AbstractAppPluginManagerService;
import com.autumn.gateway.core.service.policy.provider.IAppPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IBaseBizPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IPluginPolicyProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 系统插件管理
 * @since create 2021-08-16:16:43
 */
@Service
public class AppPluginManagerServiceImpl extends AbstractAppPluginManagerService {

  @Resource private ApplicationContext applicationContext;

  @Resource private IAppPolicyProvider appPolicyProvider;

  @Resource private IPluginPolicyProvider pluginPolicyProvider;

  /**
   * <获得策略提供者>
   *
   * @return IBaseBizPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:19
   */
  @Override
  public IBaseBizPolicyProvider getPolicyProvider() {
    return appPolicyProvider;
  }

  /**
   * <获取插件策略提供者>
   *
   * @return : com.qm.agw.core.service.policy.provider.IPluginPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:25
   */
  @Override
  public IPluginPolicyProvider getPluginPolicyProvider() {
    return pluginPolicyProvider;
  }

  /**
   * <获取上下文>
   *
   * @return : org.springframework.context.ApplicationContext
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:23
   */
  @Override
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
