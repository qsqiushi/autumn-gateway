package com.autumn.gateway.core.service.policy.provider;

import com.autumn.gateway.core.pojo.writer.WriterPolicy;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since  2021-09-04 09:57
 */
public interface IWriterPolicyProvider extends IPolicyProvider {
  /**
   * <获得写策略>
   *
   * @param
   * @return WriterPolicy
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/6 16:33
   */
  WriterPolicy getWriterPolicy();
}
