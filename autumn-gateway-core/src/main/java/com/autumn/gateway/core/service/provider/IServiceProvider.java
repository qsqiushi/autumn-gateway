package com.autumn.gateway.core.service.provider;

import com.autumn.gateway.core.service.IService;
import io.vertx.core.json.JsonObject;

/**
 * 策略供应者接口
 *
 * @author qius 2021/7/9
 */
public interface IServiceProvider {

  /**
   * <创建服务>
   *
   * @param param
   * @return : IService
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/23 10:28
   */
  IService create(JsonObject param);
}
