package com.autumn.gateway.service.impl;

import com.autumn.gateway.core.handler.UrlHandler;
import com.autumn.gateway.core.service.IListableRouterManagerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since  2021-09-01 13:21
 */
@Service
public class ListableRouterManagerServiceImpl implements IListableRouterManagerService {

  private List<UrlHandler> userHandlers = new ArrayList<>();

  /**
   * <获得可列出路由>
   *
   * @return : java.util.List<com.qm.agw.core.handler.UrlHandler>
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:16
   */
  @Override
  public List<UrlHandler> listableRouters() {
    return userHandlers;
  }

  /**
   * <注册路由>
   *
   * @param urlHandler url及处理器
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:16
   */
  @Override
  public void registerRouters(UrlHandler... urlHandler) {
    userHandlers.addAll(Arrays.asList(urlHandler));
  }

  /**
   * <>
   *
   * @param urlHandler url及处理器
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/1 13:19
   */
  @Override
  public void removeRouters(UrlHandler... urlHandler) {
    userHandlers.removeAll(Arrays.asList(urlHandler));
  }
}
