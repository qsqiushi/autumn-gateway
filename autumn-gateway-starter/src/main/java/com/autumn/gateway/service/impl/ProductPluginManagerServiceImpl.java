package com.autumn.gateway.service.impl;

import com.autumn.gateway.core.service.plugin.AbstractProductPluginManagerService;
import com.autumn.gateway.core.service.policy.provider.IBaseBizPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IPluginPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IProductPolicyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 系统插件管理
 * @since  2021-08-16 16:43
 */
@Service
public class ProductPluginManagerServiceImpl extends AbstractProductPluginManagerService {

  @Autowired
  private IProductPolicyProvider productPolicyProvider;

  @Autowired private IPluginPolicyProvider pluginPolicyProvider;

  @Autowired private ApplicationContext applicationContext;

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
    return productPolicyProvider;
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
