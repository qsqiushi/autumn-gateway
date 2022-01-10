package com.autumn.gateway.core.service.policy.provider;

import com.autumn.gateway.core.configer.IConfiger;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 基于yaml的策略提供器
 * @since  2021-08-12 18:31
 */
public interface IYamlPolicyProvider extends IPolicyProvider {

  /**
   * 初始化策略供应者
   *
   * @param params
   */
  void init(IConfiger params);
}
