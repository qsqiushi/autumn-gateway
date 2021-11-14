package com.autumn.gateway.core.service.policy.provider;

/**
 * <插件策略>
 *
 * @author qiushi
 * @since 2021/8/11 15:03
 */
public interface IPluginPolicyProvider extends IPolicyProvider {

  /**
   * <根据ID查询策略>
   *
   * @param policyId
   * @return : java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/14 14:57
   */
  String getPolicy(String policyId);
}
