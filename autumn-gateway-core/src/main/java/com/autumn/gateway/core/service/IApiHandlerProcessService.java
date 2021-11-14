package com.autumn.gateway.core.service;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 正在运行的ApiHandler计数
 * @since create 2021-09-08:15:38
 */
public interface IApiHandlerProcessService {
  /**
   * <计算正在处理API的数量>
   *
   * @param api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  int getProcessNum(Api api);
  /**
   * <正在处理API的数量+1>
   *
   * @param api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  void processIncr(Api api);
  /**
   * <计算正在处理API的数量-1>
   *
   * @param api
   * @return int
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/8 15:46
   */
  void processDecr(Api api);
}
