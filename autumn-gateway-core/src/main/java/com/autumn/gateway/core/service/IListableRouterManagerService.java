package com.autumn.gateway.core.service;

import com.autumn.gateway.core.handler.UrlHandler;

import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 可列出路由 控制
 * @since create 2021-08-30:14:18
 */
public interface IListableRouterManagerService {
  /**
   * <获得可列出路由>
   *
   * @param
   * @return : java.util.List<UrlHandler>
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:16
   */
  List<UrlHandler> listableRouters();
  /**
   * <注册路由>
   *
   * @param urlHandlers url及处理器
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:16
   */
  void registerRouters(UrlHandler... urlHandlers);

  /**
   * <>
   *
   * @param urlHandlers url及处理器
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:19
   */
  void removeRouters(UrlHandler... urlHandlers);
}
