package com.autumn.gateway.core.service.policy.provider;

import com.autumn.gateway.core.service.IService;

/**
 * 策略提供者的提供者
 *
 * @author qiushi
 * @program autumn-gateway
 * @description 策略提供者的提供者
 * @since  2021-08-12 15:38
 */
public interface IPolicyProviderProvider extends IService {
  /**
   * <创建APP 应用策略提供者>
   *
   * @param
   * @return : IAppPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 15:40
   */
  IAppPolicyProvider createAppPolicyProvider();
  /**
   * <创建 插件策略提供者>
   *
   * @param
   * @return : IPluginPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 15:42
   */
  IPluginPolicyProvider createPluginPolicyProvider();

  /**
   * <创建产品策略提供者>
   *
   * @param
   * @return : IProductPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 15:43
   */
  IProductPolicyProvider createProductPolicyProvider();
  /**
   * <创建产品分类策略提供者>
   *
   * @param
   * @return : IProductClassifyPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 15:43
   */
  IProductClassifyPolicyProvider createProductClassifyPolicyProvider();
  /**
   * <创建系统策略提供者>
   *
   * @param
   * @return : ISysPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 15:44
   */
  ISysPolicyProvider createSysPolicyProvider();
  /**
   * <创建写入>
   *
   * @param
   * @return : IWriterPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/4 13:33
   */
  IWriterPolicyProvider createWriterPolicyProvider();
}
