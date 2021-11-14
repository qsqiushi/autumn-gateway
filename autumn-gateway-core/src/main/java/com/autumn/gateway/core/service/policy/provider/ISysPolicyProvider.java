package com.autumn.gateway.core.service.policy.provider;

import java.util.Map;

/**
 * @program autumn-gateway
 * @description 系统策略
 * @author qiushi
 * @since create 2021-08-12:14:11
 */
public interface ISysPolicyProvider extends IBaseBizPolicyProvider {
  /**
   * <获取域名>
   *
   * @param
   * @return java.lang.String
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/12 19:36
   */
  String getDomain();

  /**
   * <获取所有系统策略>
   *
   * @param
   * @return java.util.Map<java.lang.String,java.lang.String>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/13 08:27
   */
  Map<String, String> getSysPolicy();
}
