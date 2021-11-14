package com.autumn.gateway.core.service.router;

import com.autumn.gateway.core.handler.UrlHandler;
import com.autumn.gateway.core.service.IService;

import java.util.List;

/** @author qiushi */
public interface IRouterService extends IService {

  /**
   * <自定义路径处理器>
   *
   * @param
   * @return : java.util.List<UrlHandler>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/31 16:12
   */
  List<UrlHandler> userDefinedRouterHandler();
}
