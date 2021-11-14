package com.autumn.gateway.service.impl;

import com.autumn.gateway.core.server.IServerManager;
import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.core.service.policy.provider.IAppPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IProductClassifyPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IProductPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.ISysPolicyProvider;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import com.autumn.gateway.core.service.server.IServer;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-28:15:54
 */
@Slf4j
@Component
public class ServerManagerImpl implements IServerManager {

  @Resource private ApplicationContext applicationContext;

  private List<IServer> servers;

  /**
   * <新增服务>
   *
   * @param server 服务
   * @return : java.util.List<com.qm.agw.core.service.server.IServer>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/10 10:30
   */
  @Override
  public List<IServer> addServer(IServer server) {
    if (servers == null) {
      servers = new ArrayList<>();
    }
    servers.add(server);
    return servers;
  }

  @Override
  public Future<List<IServer>> startServers() {

    return Future.future(
        event -> {
          // 若缺失下面组件注册的bean会导致startServers报错 所以直接 System.exit
          if (applicationContext.getBeansOfType(IApiRegisterService.class).size() <= 0) {
            log.error("缺少API注册组件");
            event.fail("缺少API注册组件");
            return;
          }

          if (applicationContext.getBeansOfType(IApiDiscovererService.class).size() <= 0) {
            log.error("缺少API发现组件");
            event.fail("缺少API发现组件");
            return;
          }

          if (applicationContext.getBeansOfType(IVertxManagerService.class).size() <= 0) {
            log.error("缺少Vertx实例组件");
            event.fail("缺少Vertx实例组件");
            return;
          }

          if (applicationContext.getBeansOfType(IAppPolicyProvider.class).size() <= 0) {
            log.error("缺少应用策略实例组件");
            event.fail("缺少应用策略实例组件");
            return;
          }

          if (applicationContext.getBeansOfType(ISysPolicyProvider.class).size() <= 0) {
            log.error("缺少系统策略实例组件");
            event.fail("缺少系统策略实例组件");
            return;
          }

          if (applicationContext.getBeansOfType(IProductPolicyProvider.class).size() <= 0) {
            log.error("缺少产品策略实例组件");
            event.fail("缺少产品策略实例组件");
            return;
          }

          if (applicationContext.getBeansOfType(IProductClassifyPolicyProvider.class).size() <= 0) {
            log.error("缺少产品分类策略实例组件");
            event.fail("缺少产品分类策略实例组件");
            return;
          }

          if (servers.size() <= 0) {
            log.error("缺少核心服务组件");
            event.fail("缺少核心服务组件");
            return;
          }

          for (IServer server : servers) {
            server.start();
          }
          event.complete(servers);
        });
  }

  @Override
  public void stopServer(IServer server) {
    server.stop();
  }

  @Override
  public Future<Void> stopServers() {
    return Future.future(
        event -> {
          for (IServer server : servers) {
            server.stop();
          }
          event.complete();
        });
  }

  @Override
  public Integer getSize() {
    if (servers == null) {
      servers = new ArrayList<>();
    }
    return servers.size();
  }

  /**
   * <移除server>
   *
   * @param server
   * @return void
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/9 15:05
   */
  @Override
  public void remove(IServer server) {
    servers.remove(server);
  }

  /**
   * <移除所有server>
   *
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/24 14:41
   */
  @Override
  public void removeAll() {
    servers = null;
    servers = new ArrayList<>();
  }
}
